package com.itxiaowu.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.itxiaowu.adapter.PathAdapter;
import com.itxiaowu.fileOpreation.FileOperation;
import com.itxiaowu.utils.StringUtils;
import com.itxiaowu.view.TitleBar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PathActivity extends Activity {
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	private String uniqueId;
    
    TitleBar titleBar;
    EditText et_path;
    List<String> keys;
    PathAdapter adaper;
    ListView lv_path;
	Builder builder;
	Button btnSubmit;
	
	private boolean isFirstWrite=true;
	private String prevFileName;
	private Button btnPathHistory;
	
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_path);

		preferences=getSharedPreferences("pathActivityPre", Context.MODE_PRIVATE);
		editor=preferences.edit();
		prevFileName=preferences.getString("prevFileName", "");
		
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		 
	    final String tmDevice, tmSerial, tmPhone, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	 
	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    uniqueId = deviceUuid.toString();
		
		getActionBar().hide();
		keys=new ArrayList<String>();
		titleBar=(TitleBar) findViewById(R.id.title_bar);
        titleBar.setMode(TitleBar.TitleBarMode.LEFT_BUTTON);
        titleBar.setDisplayAsBack(true);
        titleBar.setTitle("物流扫描");
        
        btnSubmit=(Button) findViewById(R.id.btn_submitPath);
        bindSubmit();
        lv_path=(ListView) findViewById(R.id.lv_path);
        adaper=new PathAdapter(this, getResources());
        adaper.setKeyList(keys);

        lv_path.setAdapter(adaper);
        bindTextChanged();
        
		btnPathHistory=(Button) findViewById(R.id.btn_pathHistory);
		btnPathHistory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent=new Intent(PathActivity.this,PathHistoryActivity.class);
				startActivity(intent);
			}
		});
	}
        
	
	

	@Override
	protected void onPause() {

		editor.putString("prevFileName", prevFileName);
		editor.commit();
		super.onPause();
	}
	
	
	
	private void bindSubmit() {

		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!keys.isEmpty()){
					String saveContent="";

					for (int i = 0; i < keys.size(); i++) {
						
						saveContent+=keys.get(i)+",";													
					}
					saveContent=saveContent.substring(0, saveContent.lastIndexOf(','));
					
					Boolean result=writeFile(saveContent);
					if (result) {
						keys.clear();
						adaper.notifyDataSetChanged();
					}
				}

			}
		});
	}


    private boolean writeFile(String content) {
        try {
        	
        		String currFileName= FileOperation.createNewFileName("/Path_");
        		String deviceString="";
        		if (!currFileName.equals(prevFileName)) {
        			deviceString="DeviceCode:"+uniqueId+"\r\n";
				}
        		prevFileName=currFileName;
        		
                File file = new File(currFileName);
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                content=deviceString+str+","+content+"\r\n";

                bw.write(content);
                bw.flush();
                
//        	}
 
            return true;
        } catch (Exception ex) {
//            logger.e(ex);
        	Log.d("ZXW", "MainActivity writeFile");
            return false;
        }
    }



	private void bindTextChanged(){
		et_path= (EditText) findViewById(R.id.et_path);
		et_path.requestFocus();
		

		et_path.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String string=new StringBuilder(s).toString();
				try {

					if(string.isEmpty()||string=="\n"){
						return;
					}
                    if (keys != null && keys.size() > 0) {
                        for (int i = 0; i < keys.size(); i++) {
                            if (StringUtils.replaceBlank(StringUtils.getLastString(string))
                            		.equals(StringUtils.getLastString(StringUtils.replaceBlank(keys.get(i))))) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                       getString(R.string.key_exist) , Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER , 0, 0);
                                toast.show();
                                return;
                            }
                        }
                    }
                    keys.add(StringUtils.replaceBlank(StringUtils.getLastString(string)));

                    adaper.notifyDataSetChanged();
					
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
	}
	
	private void BindListViewEvent(){
		lv_path.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int po=position;
				builder=new Builder(PathActivity.this);
				
				builder.setPositiveButton("ɾ��",new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				})
				.create().show();
				
						
				return true;
			}
			
		});
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.path, menu);
//		return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
