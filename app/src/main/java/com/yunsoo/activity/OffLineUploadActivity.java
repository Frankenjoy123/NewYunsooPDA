package com.yunsoo.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yunsoo.activity.R;
import com.yunsoo.view.TitleBar;

public class OffLineUploadActivity extends Activity {
    TitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_line_upload);
        getActionBar().hide();
        titleBar= (TitleBar) findViewById(R.id.off_line_upload_title_bar);
        titleBar.setMode(TitleBar.TitleBarMode.LEFT_BUTTON);
        titleBar.setDisplayAsBack(true);
        titleBar.setTitle("");
    }

}
