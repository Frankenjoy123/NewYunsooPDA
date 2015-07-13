package com.yunsoo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.ListView;

import com.yunsoo.adapter.PathAdapter;
import com.yunsoo.fileOpreation.FileRead;
import com.yunsoo.view.TitleBar;

import java.util.List;

public class PathHistoryActivity extends Activity {
	private ListView lv_pathHistory;
	private PathAdapter historyAdapter;
	private List<String> historyList;
	private FileRead historyFileReader;
    private TitleBar titleBar;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_path_history);

        getActionBar().hide();
        titleBar=(TitleBar) findViewById(R.id.pathHistory_title_bar);
        titleBar.setMode(TitleBar.TitleBarMode.LEFT_BUTTON);
        titleBar.setDisplayAsBack(true);
        titleBar.setTitle("物流扫描历史");

        try {
            historyFileReader=new FileRead("Path_");
            Time time=new Time();
            time.setToNow();
            historyList=historyFileReader.getKeyList(time);

            lv_pathHistory=(ListView) findViewById(R.id.lv_pathHistory);
            historyAdapter=new PathAdapter(this, getResources());
            historyAdapter.setKeyList(historyList);
            lv_pathHistory.setAdapter(historyAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
