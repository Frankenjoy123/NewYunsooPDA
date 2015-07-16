package com.yunsoo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.yunsoo.activity.R;
import com.yunsoo.adapter.FileSyncAdapter;
import com.yunsoo.fileOpreation.FileOperation;
import com.yunsoo.manager.DeviceManager;
import com.yunsoo.manager.FileManager;
import com.yunsoo.service.FileUpLoadService;
import com.yunsoo.sqlite.MyDataBaseHelper;
import com.yunsoo.util.Constants;
import com.yunsoo.view.TitleBar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PackSyncActivity extends BaseActivity {
    private MyDataBaseHelper dataBaseHelper;
    private ListView lv_pack_sync;
    private TitleBar titleBar;

    private FileSyncAdapter adapter;

    private int minIndex;
    private int maxIndex;

    private List<String> fileNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack_sync);

        getActionBar().hide();

        init();
        createPackFile();
        getPackFileNames();
        uploadFiles();
    }

    private void uploadFiles() {
        String folderName = android.os.Environment.getExternalStorageDirectory() +
                Constants.YUNSOO_FOLDERNAME+Constants.PACK_SYNC_TASK_FOLDER;
        File pack_task_folder = new File(folderName);
        File[] files=pack_task_folder.listFiles();
        for(File file:files){
            FileUpLoadService service=new FileUpLoadService(file.getAbsolutePath());
            service.start();

        }
//        for (int i=0;i<files.length;i++){
//            String path=files[i].getAbsolutePath();
//            FileUpLoadService service=new FileUpLoadService(path);
//            service.start();
//        }

    }

    private void getPackFileNames() {
        try {
            String folderName = android.os.Environment.getExternalStorageDirectory() +
                    Constants.YUNSOO_FOLDERNAME+Constants.PACK_SYNC_TASK_FOLDER;
            File pack_task_folder = new File(folderName);
            String[] packFiles= pack_task_folder.list();
            if (packFiles!=null&&packFiles.length>0){
                for (int i=0;i<packFiles.length;i++){
                    fileNames.add(packFiles[i]);
                }
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        lv_pack_sync= (ListView) findViewById(R.id.lv_pack_sync);
        titleBar= (TitleBar) findViewById(R.id.pack_sync_title_bar);
        titleBar.setTitle(getString(R.string.pack_sync));
        titleBar.setMode(TitleBar.TitleBarMode.BOTH_BUTTONS);
        titleBar.setDisplayAsBack(true);
        titleBar.setRightButtonText(getString(R.string.sync));
        adapter=new FileSyncAdapter(this);
        fileNames=new ArrayList<>();
        adapter.setFileNames(fileNames);
        lv_pack_sync.setAdapter(adapter);

        titleBar.setOnRightButtonClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileNames!=null&&fileNames.size()>0){
                    String[] titleArray = new String[]{getString(R.string.off_line_upload),
                    getString(R.string.wifi_upload)};
                    AlertDialog dialog = new AlertDialog.Builder(PackSyncActivity.this)
                            .setItems(titleArray, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (i==0){
                                        Intent intent=new Intent(PackSyncActivity.this,OffLineUploadActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            })
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.cancel, null).create();
                    dialog.show();
                }
            }
        });
    }

    private void createPackFile() {
        dataBaseHelper=new MyDataBaseHelper(this, Constants.SQ_DATABASE,null,1);
        Cursor cursor=dataBaseHelper.getReadableDatabase().rawQuery("select * from pack where status='0'",null);

        if (cursor.getCount()>0){

            StringBuilder builder=new StringBuilder();

            while (cursor.moveToNext()){
                if (cursor.isFirst()){
                    minIndex=cursor.getInt(0);
                }else if (cursor.isLast()){
                    maxIndex=cursor.getInt(0);
                }
                builder.append(cursor.getString(3));
                builder.append(",");
                builder.append(cursor.getString(1));
                builder.append(",");
                builder.append(cursor.getString(2));
                builder.append("\r\n");
            }
            dataBaseHelper.getWritableDatabase().execSQL("update pack set status='1' where _id>=? and _id<=?",
                    new String[]{String.valueOf(minIndex),String.valueOf(maxIndex)});

            try {

                String folderName = android.os.Environment.getExternalStorageDirectory() +
                        Constants.YUNSOO_FOLDERNAME+Constants.PACK_SYNC_TASK_FOLDER;
                File pack_task_folder = new File(folderName);
                if (!pack_task_folder.exists())
                    pack_task_folder.mkdirs();

                StringBuilder fileNameBuilder=new StringBuilder("pack_");
                fileNameBuilder.append(DeviceManager.getInstance().getDeviceId());
                fileNameBuilder.append("_");
                fileNameBuilder.append(FileManager.getInstance().getPackFileLastIndex() + 1);

                File file=new File(pack_task_folder,fileNameBuilder.toString());

                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

                bw.write(builder.toString());
                bw.flush();

                FileManager.getInstance().savePackFileIndex(FileManager.getInstance().getPackFileLastIndex() + 1);


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }


//    private boolean getFileBySQLite() {
//        try {
//            File file = new File(FileOperation.createNewFileName("/Pack_"));
//            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
//
//            String str = formatter.format(curDate);
//            content = str + "," + content;
//            content += "\r\n";
////
//            bw.write(content);
//            bw.flush();
//            return true;
//        } catch (Exception ex) {
////            logger.e(ex);
//            Log.d("ZXW", "MainActivity writeFile");
//            return false;
//        }
//    }



}
