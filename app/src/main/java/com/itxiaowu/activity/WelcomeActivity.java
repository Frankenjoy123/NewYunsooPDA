package com.itxiaowu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WelcomeActivity extends Activity {
	private Button btn_package_page;
	private Button btn_path_page;
    private Button btn_fix_pack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		getActionBar().hide();
		btn_package_page=(Button) findViewById(R.id.btn_package_page);
		btn_path_page=(Button) findViewById(R.id.btn_path_page);
        btn_fix_pack= (Button) findViewById(R.id.btn_fix_pack);
        btn_fix_pack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomeActivity.this,FixPackActivity.class);
                startActivity(intent);
            }
        });
		btn_package_page.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent=new Intent(WelcomeActivity.this,ScanActivity.class);
				startActivity(intent);
			}
		});
		
		btn_path_page.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent=new Intent(WelcomeActivity.this,PathActivity.class);
				startActivity(intent);
			}
		});
	}

}
