package com.itxiaowu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.itxiaowu.adapter.PackageHistoryAdapter;
import com.itxiaowu.fileOpreation.PackDetailFileRead;
import com.itxiaowu.unity.PackageDetail;
import com.itxiaowu.view.TitleBar;

import java.util.List;

public class PackedHistoryActivity extends Activity {
	
	private ListView lv;
	private List<PackageDetail> detailList;
	private PackageHistoryAdapter adapter;
	private TitleBar titleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_packed_history);
		
		getActionBar().hide();
		titleBar=(TitleBar) findViewById(R.id.packHistory_title_bar);
        titleBar.setMode(TitleBar.TitleBarMode.LEFT_BUTTON);
        titleBar.setDisplayAsBack(true);
        titleBar.setTitle("打包历史");
		
		lv=(ListView) findViewById(R.id.lv_hasPacked);


        try {
            PackDetailFileRead detailFileReader=new PackDetailFileRead("Pack_");
            Time time=new Time();
            time.setToNow();
            detailList=detailFileReader.getPackageDetailList(time);

            adapter=new PackageHistoryAdapter(this, getResources());
            adapter.setPackageDetailList(detailList);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Intent intent=new Intent(PackedHistoryActivity.this,
                            PackDetail.class);
                    PackageDetail detail=detailList.get(position);

                    Bundle bundle=new Bundle();
                    bundle.putSerializable("detail", detail);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

	}
}
