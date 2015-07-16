package com.yunsoo.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunsoo.activity.R;

public class PathMainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_main);
        getActionBar().hide();
        setupActionItems();
    }

    private void setupActionItems() {

        buildViewContent(this.findViewById(R.id.rl_path_scan), R.drawable.ic_delivery, R.string.path_scan);
        buildViewContent(this.findViewById(R.id.rl_path_sync), R.drawable.ic_synchronize, R.string.sync_path);
//        buildViewContent(this.findViewById(R.id.rl_modify_package), R.drawable.ic_modify_package, R.string.modify_package);

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
            case R.id.rl_path_scan:
                Intent intent1=new Intent(PathMainActivity.this,PathActivity.class);
                startActivity(intent1);
                break;
//            case R.id.rl_path_sync:
//                Intent intent2=new Intent(MainActivity.this,PathActivity.class);
//                startActivity(intent2);
//                break;
//            case R.id.rl_modify_package:
//                Intent intent3=new Intent(MainActivity.this,FixPackActivity.class);
//                startActivity(intent3);
//                break;
        }
    }
}
