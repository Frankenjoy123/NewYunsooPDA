package com.itxiaowu.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itxiaowu.fileOpreation.FileOperation;
import com.itxiaowu.unity.ListItem;
import com.itxiaowu.view.TitleBar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanActivity extends Activity {

	private TextView input;
	
	private ImageView downArrow;
	
	private ProgressBar progressBar;

	private int[] standards=new int[]{5,10,20,30,50};

	private int standard=5;

	private int count=0;

	private int finishedBags=0;
	
	private List<String> msgList;

	List<ListItem> listItems;
	
	private PopupWindow popWin;

	private ListView listView;
	private TextView progressText;
	
    EditText editText;
    private EditText package_key_EditText;
    private TextView finish_package_text;
    
    private Button btn_packageActivityFinish;
    
    private LinearLayout linearLayout_standard;
    
    SoundPool soundPool;
    HashMap<Integer, Integer> soundMap;

	private TitleBar titleBar;
	
	private Button btn_getPackages;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("ZXW", "MainActivity onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().hide();
		titleBar=(TitleBar) findViewById(R.id.package_title_bar);
        titleBar.setMode(TitleBar.TitleBarMode.LEFT_BUTTON);
        titleBar.setDisplayAsBack(true);
        titleBar.setTitle("打包扫描");
		
		listItems=new ArrayList<ListItem>();
		input = (TextView) findViewById(R.id.input);
		package_key_EditText=(EditText) findViewById(R.id.package_key_edittext);
		package_key_EditText.setTextColor(0x00000000);
		
		finish_package_text=(TextView) findViewById(R.id.finish_package_text);
		
		downArrow = (ImageView) findViewById(R.id.down_arrow);
		progressBar=(ProgressBar) findViewById(R.id.progressBar1);
		progressText=(TextView) findViewById(R.id.progressText);
		linearLayout_standard=(LinearLayout) findViewById(R.id.linearLayout_standard);
		btn_getPackages=(Button) findViewById(R.id.btn_getPackages);
		bindGetHistoryPackages();


		
		soundPool=new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
		soundMap=new HashMap<Integer, Integer>();
		soundMap.put(1, soundPool.load(getApplicationContext(), R.raw.short_sound, 1));
		soundMap.put(2, soundPool.load(getApplicationContext(), R.raw.long_sound, 1));

		bindTextChanged();
		bindPackageKeyChanged();
		progressBar.setMax(5);
		progressBar.setProgress(0);
		
		msgList = new ArrayList<String>();
	
		for (int i = 0; i < 5; i++) {
			msgList.add(""+standards[i]+"个/箱");
		}
		
		initListView();
		
		linearLayout_standard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				System.out.println("=======");

				popWin = new PopupWindow(ScanActivity.this);
				popWin.setWidth(input.getWidth());
				popWin.setHeight(300);
				popWin.setContentView(listView);
				popWin.setOutsideTouchable(true);
				popWin.showAsDropDown(input, 0, 0);
			}
		});	
		

		progressBar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle listItems_bundle=new Bundle();
				ArrayList list=new ArrayList();				
				ArrayList<String> titlesArrayList=new ArrayList<String>();
				for (int i = 0; i < listItems.size(); i++) {
					ListItem item=listItems.get(i);
					titlesArrayList.add(item.getTitle());
				}	
				list.add(titlesArrayList);
				Intent intent=new Intent(ScanActivity.this, ListActivity.class);
				intent.putParcelableArrayListExtra("titles", list);
				intent.putExtra("numPerGroup", standard);
				Log.d("ZXW", "createIntent");
				startActivityForResult(intent, 0);
				Log.d("ZXW", "MainActivity startActivityForResult");
			}
		});
	}
	
	private void bindGetHistoryPackages() {

		btn_getPackages.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ScanActivity.this, PackedHistoryActivity.class);
				startActivity(intent);
			}
		});
		
	
	}

	@Override
	protected void onStart() {

		super.onStart();
		Log.d("ZXW", "MainActivity onStart");
	}
	@Override
	protected void onRestart() {
		// TODO �Զ����ɵķ������
		super.onRestart();
		Log.d("ZXW", "MainActivity onRestart");
	}

	
	@Override
	protected void onResume() {

		super.onResume();
		Log.d("ZXW", "MainActivity onResume");
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("ZXW", "MainActivity onActivityResult");
		if(requestCode==0&&resultCode==0){
			boolean isClearAll= data.getBooleanExtra("isClearAll", false);
			if(isClearAll){
				listItems.clear();
				count=0;
			}
			else{
		        ArrayList list=data.getParcelableArrayListExtra("delete_titles");
		        ArrayList<String> delete_titles=(ArrayList<String>) list.get(0);

		        for (int i = 0; i < delete_titles.size(); i++) {
		        	String string=delete_titles.get(i);
		        	for (int j = 0; j < listItems.size(); j++) {
		        		if(listItems.get(j).getTitle().equals(delete_titles.get(i))){
		        			listItems.remove(j);
		        			count--;
		        			
		        		}
						
					}
				}
			}
			if(count<standard){
				package_key_EditText.clearFocus();
				editText.requestFocus();
			}
	        progressBar.setProgress(count);
	        progressText.setText("当前进度"+count+"/"+standard);
		}
	}
	
	
	private void bindTextChanged(){
		editText= (EditText) findViewById(R.id.editText);
		editText.requestFocus();
		
		package_key_EditText.clearFocus();
		editText.addTextChangedListener(new TextWatcher() {
			
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
//					if(s.toString().isEmpty()||s.toString()=="\n"){
//						return;
//					}
					if(string.isEmpty()||string=="\n"){
						return;
					}
                    if (listItems != null && listItems.size() > 0) {
                        for (int i = 0; i < listItems.size(); i++) {
                            ListItem item = listItems.get(i);
                            if (replaceBlank(getLastString(string)).equals(getLastString(replaceBlank(item.getTitle())))) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "该码已存在", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM , 0, 0);
                                toast.show();
                                //isValideScan=false;
                                return;
                            }
                        }
                    }
					ListItem item=new ListItem();
					item.setTitle(replaceBlank(getLastString(string)));
					listItems.add(item);
					count++;
					progressBar.setProgress(count);
					progressText.setText("当前进度:"+count+"/"+standard);

					if (count!=standard) {
						soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
					}
					else if(count==standard) {
						soundPool.play(soundMap.get(2), 1, 1, 0, 0, 1);
						Log.d("ZXW", "count==standard");
						editText.clearFocus();
						package_key_EditText.requestFocus();
						
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
	}
    private void bindPackageKeyChanged() {
    	package_key_EditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int newcount) {

		
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				Log.d("ZXW", "scanPackageKey afterTextChanged");
				/**
				 * String newNumber =new StringBuilder(dialEtBox.getText().toString()).
				 * append(inputNumber).toString();  
				 */
				
				
				String string=new StringBuilder(s.toString()).toString();

				String saveContent="";
				saveContent+=replaceBlank(getLastString(string))+",";
				for (int i = 0; i < listItems.size(); i++) {
					ListItem item=listItems.get(i);
					item.setPackage(true);
					
					saveContent+=item.getTitle()+",";													
				}
				saveContent=saveContent.substring(0, saveContent.lastIndexOf(','));
				Boolean result=writeFile(saveContent);
				Log.d("ZXW", "scanPackageKey result "+result.toString());
		        if (result) {

		            Toast toast = Toast.makeText(getApplicationContext(),
		                    "打包完成", Toast.LENGTH_LONG);
		            toast.setGravity(Gravity.CENTER , 0, 0);
		            toast.show();
		            finishedBags++;
		            finish_package_text.setText("今日已打包： "+finishedBags+"箱");
		            
		            listItems.clear();
		            count=0;
		            progressBar.setProgress(count);
		            progressText.setText("当前进度:"+count+"/"+standard);

		            editText.requestFocus();
		            package_key_EditText.clearFocus();
		            
		            
		            	
		        }	
			}
		});



	}

    
    private boolean writeFile(String content) {
        try {
            File file = new File(FileOperation.createNewFileName("/Pack_"));
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            content = str + "," + content;
            content += "\r\n";
            bw.write(content);
            bw.flush();
            return true;
        } catch (Exception ex) {
//            logger.e(ex);
        	Log.d("ZXW", "MainActivity writeFile");
            return false;
        }
    }

	private String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    
    private String getLastString(String str) {
        String[] splitStr = str.split("/");
        int len = splitStr.length;

        if(len == 0)
            return str;

        String result = splitStr[len - 1];
        return result;
    }
	

	private void initListView() {
		listView = new ListView(this);
		listView.setBackgroundResource(R.drawable.listview_background);

		listView.setVerticalScrollBarEnabled(false);
		listView.setAdapter(new MyListAdapter());
	}

	private class MyListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return msgList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				convertView = View.inflate(getApplicationContext(), R.layout.list_item_dropdown, null);
				holder = new ViewHolder();
				holder.tv_msg =(TextView) convertView.findViewById(R.id.tv_list_item1);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.tv_msg.setText(msgList.get(position));
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					input.setText(msgList.get(position));
					
					standard=standards[position];
					progressBar.setMax(standard);
					progressText.setText("当前进度:"+count+"/"+standard);
					popWin.dismiss();
				}
			});
			
			return convertView;
		}
	}

	private class ViewHolder{
		TextView tv_msg;
	}
	
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        Log.d("ZXW", "MainActivity onPause");
        try {
            SharedPreferences mySharedPreferences = getSharedPreferences("MainActivity",
                    Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.clear();
            editor.putString("ItemSize", String.valueOf(listItems.size()));
            for (int i = 0; i < listItems.size(); i++) {
                ListItem item = listItems.get(i);
                editor.putString(String.valueOf(i), replaceBlank(item.getTitle()));
               // editor.putString(String.valueOf(i) + "IsPackage", String.valueOf(item.getPackage()));
            }
            editor.commit();
        }
        catch(Exception ex)
        {
//            logger.e(ex);
        }
    }
    
    @Override
    protected void onStop() {

    	super.onStop();

    }
	
}