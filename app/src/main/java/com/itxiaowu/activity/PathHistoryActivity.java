package com.itxiaowu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.ListView;

import com.itxiaowu.adapter.PathAdapter;
import com.itxiaowu.fileOpreation.FileRead;

import java.util.List;

public class PathHistoryActivity extends Activity {
	private ListView lv_pathHistory;
	private PathAdapter historyAdapter;
	private List<String> historyList;
	private FileRead historyFileReader;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_path_history);
		historyFileReader=new FileRead("Path_");
		Time time=new Time();
		time.setToNow();
		historyList=historyFileReader.getKeyList(time);
		
		lv_pathHistory=(ListView) findViewById(R.id.lv_pathHistory);
		historyAdapter=new PathAdapter(this, getResources());
		historyAdapter.setKeyList(historyList);
		lv_pathHistory.setAdapter(historyAdapter);
		
	}
}
