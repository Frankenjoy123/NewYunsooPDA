package com.yunsoo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements OnClickListener{

    private RelativeLayout rl_pack_scan;
    private RelativeLayout rl_path_scan;
    private RelativeLayout rl_modify_package;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().hide();
        setupActionItems();
	}

    private void setupActionItems() {

        buildViewContent(this.findViewById(R.id.rl_pack_scan), R.drawable.ic_pack, R.string.pack_scan);
        buildViewContent(this.findViewById(R.id.rl_path_scan), R.drawable.ic_delivery, R.string.path_scan);
        buildViewContent(this.findViewById(R.id.rl_modify_package), R.drawable.ic_modify_package, R.string.modify_package);

    }

    private void buildViewContent(View view, int imageResourceId, int textResourceId) {
        ImageView iv = (ImageView) view.findViewById(R.id.iv_image);
        iv.setImageResource(imageResourceId);
        TextView tv = (TextView) view.findViewById(R.id.tv_action_name);
        tv.setText(textResourceId);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_pack_scan:
                Intent intent1=new Intent(MainActivity.this,ScanActivity.class);
				startActivity(intent1);
                break;
            case R.id.rl_path_scan:
				Intent intent2=new Intent(MainActivity.this,PathActivity.class);
				startActivity(intent2);
                break;
            case R.id.rl_modify_package:
                Intent intent3=new Intent(MainActivity.this,FixPackActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
