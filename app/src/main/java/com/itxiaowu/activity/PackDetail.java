package com.itxiaowu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.itxiaowu.adapter.ProductInPackageAdapter;
import com.itxiaowu.unity.PackageDetail;

public class PackDetail extends Activity {
	private TextView tv_packageID;
	private ListView lv_productInPack;
	private ProductInPackageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pack_detail);
		
		Intent intent=getIntent();
		PackageDetail detail=(PackageDetail) intent.getSerializableExtra("detail");
		tv_packageID=(TextView) findViewById(R.id.tv_packageID);
		lv_productInPack=(ListView) findViewById(R.id.lv_productInPack);
		tv_packageID.setText(detail.getPackageId());
		
		adapter=new ProductInPackageAdapter(PackDetail.this, getResources());
		adapter.setProductIdList(detail.getProductIdList());
		
		lv_productInPack.setAdapter(adapter);
	}
}
