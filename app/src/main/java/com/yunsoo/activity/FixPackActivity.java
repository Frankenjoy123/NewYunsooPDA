package com.yunsoo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.yunsoo.adapter.ProductInPackageAdapter;
import com.yunsoo.fileOpreation.FileOperation;
import com.yunsoo.fileOpreation.PackDetailFileRead;
import com.yunsoo.sqlite.MyDataBaseHelper;
import com.yunsoo.util.StringUtils;
import com.yunsoo.view.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FixPackActivity extends Activity {
    private TitleBar titleBar;
    private EditText et_fix_barcode;
    private List<String> productCodes;
    private List<String> originalCodes;
    private ProductInPackageAdapter adapter;
    private ListView lv_fix_products;
    private EditText et_get_packCode;
    private EditText et_get_productCode;
    private AlertDialog.Builder builder;
    private int originalSize;
    PackDetailFileRead fileRead;
    private File fixedFile;

    private String correctString;

    private MyDataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_pack);
//        correctString="";
        getActionBar().hide();
        dataBaseHelper=new MyDataBaseHelper(this,"yunsoo_pda",null,1);
        titleBar=(TitleBar) findViewById(R.id.fix_title_bar);
        titleBar.setMode(TitleBar.TitleBarMode.BOTH_BUTTONS);
        titleBar.setDisplayAsBack(true);
        titleBar.setTitle("修改包装");
        titleBar.setRightButtonText("完成");
        try {
            titleBar.setOnRightButtonClickedListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(originalSize>0){

//                        fixedFile=fileRead.getFixedFile();
//                        StringBuilder sb=new StringBuilder("");
//                        sb.append(fileRead.getFixPreString());
                        StringBuilder builder1=new StringBuilder();

                        for (int i=0;i<productCodes.size();i++){
//                            sb.append(","+productCodes.get(i));
                            builder1.append(productCodes.get(i));
                            if (i<productCodes.size()-1){
                                builder1.append(",");
                            }
                        }
                        dataBaseHelper.getWritableDatabase().execSQL("update pack set product_keys=?",new String[]{builder1.toString()});

//                        FileOperation.replaceTxtByStr(fileRead.getFixedLineString(),sb.toString(),fixedFile);
                        finish();

                    }

                }
            });

        } catch (Exception e) {
            finish();
            e.printStackTrace();
        }

        productCodes=new ArrayList<String>();
        originalCodes=new ArrayList<String>();
        adapter=new ProductInPackageAdapter(FixPackActivity.this,getResources());
        adapter.setProductIdList(productCodes);
        lv_fix_products= (ListView) findViewById(R.id.lv_fix_products);
        lv_fix_products.setAdapter(adapter);
        et_fix_barcode= (EditText) findViewById(R.id.et_fix_barcode);
        et_get_packCode= (EditText) findViewById(R.id.et_get_packCode);
        et_get_productCode= (EditText) findViewById(R.id.et_get_productCode);
        et_get_packCode.requestFocus();
        bindPackBarcodeChanged();
        bindProductBarcodeChanged();
        bindListViewEvent();
    }



    private void bindListViewEvent(){
        lv_fix_products.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                final int po=position;
                builder=new AlertDialog.Builder(FixPackActivity.this);

                builder.setPositiveButton("删除",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        productCodes.remove(po);
                        adapter.notifyDataSetChanged();

                    }
                })
                 .create().show();


                return true;
            }

        });
    }

    private void bindProductBarcodeChanged() {
        et_get_productCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(productCodes.size()<originalSize){
                    String string = new StringBuilder(s).toString();
                    string=StringUtils.getLastString(StringUtils.replaceBlank(string));
                    productCodes.add(string);

                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "该包已装满，请按完成", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER , 0, 0);
                    toast.show();
                }

            }
        });
    }

    private void bindPackBarcodeChanged() {
        et_get_packCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = new StringBuilder(s).toString();
                string=StringUtils.getLastString(StringUtils.replaceBlank(string));
                et_fix_barcode.setText(string);
                productCodes.clear();
//                adapter.notifyDataSetChanged();
                Cursor cursor=dataBaseHelper.getReadableDatabase().rawQuery("select * from pack where pack_key=?", new String[]{string});
                if(cursor.getCount()>0){
                    et_get_packCode.clearFocus();
                    et_get_productCode.requestFocus();
                    while (cursor.moveToNext()){
                        String products=cursor.getString(2);
                        String[] arrayStrings=products.split(",");

                        for(int j=0;j<arrayStrings.length;j++){
                            productCodes.add(arrayStrings[j]);
                        }
                    }
                    originalCodes.addAll(productCodes);
                    originalSize=productCodes.size();
                    adapter.notifyDataSetChanged();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "找不到该包装码", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER , 0, 0);
                    toast.show();
                }



//                try {
////                    fileRead=new PackDetailFileRead("Pack_");
////                    correctString=fileRead.getFixedLineString();
////
////                    productCodes.addAll(fileRead.getProductsByPackCode(string));
//
//                    originalCodes.addAll(productCodes);
//                    originalSize=productCodes.size();
////                    for(int i=0;i<originalSize;i++){
////                        correctString.replace(","+originalCodes.get(i),"");
////                    }
//
//                    adapter.notifyDataSetChanged();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            "找不到该包装码", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER , 0, 0);
//                    toast.show();
//                }
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_fix_pack, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
