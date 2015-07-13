package com.yunsoo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yunsoo.view.TitleBar;

public class SettingActivity extends Activity {
    private TitleBar titleBar;
    private EditText et_count;
    private EditText et_standard;
    private int finishedBags;
    private int standard;
    private Intent intent;
    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getActionBar().hide();

        titleBar=(TitleBar) findViewById(R.id.setting_title_bar);
        titleBar.setMode(TitleBar.TitleBarMode.LEFT_BUTTON);
        titleBar.setDisplayAsBack(true);
        titleBar.setTitle(getString(R.string.settings));

        intent=getIntent();
        finishedBags=intent.getIntExtra("finishedBags",0);
        standard=intent.getIntExtra("standard",0);

        et_count= (EditText) findViewById(R.id.et_count);
        et_standard= (EditText) findViewById(R.id.et_standard);
        confirm=(Button)findViewById(R.id.btn_setting_confirm);
        et_count.setText(String.valueOf(finishedBags));
        et_standard.setText(String.valueOf(standard));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("edit_count",
                        Integer.valueOf(et_count.getText().toString()));
                intent.putExtra("et_standard",
                        Integer.valueOf(et_standard.getText().toString()));
                SettingActivity.this.setResult(200,intent);
                SettingActivity.this.finish();
            }
        });


    }


}
