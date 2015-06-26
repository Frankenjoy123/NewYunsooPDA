package com.itxiaowu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONObject;

import com.itxiaowu.annotation.ViewById;
//import com.itxiaowu.manager.DeviceGeoLocationManager;
import com.itxiaowu.manager.SessionManager;
import com.itxiaowu.service.DataServiceImpl;
import com.itxiaowu.util.DensityUtil;


public class WelcomeActivity extends BaseActivity{

    @ViewById(id = R.id.iv_icon)
    ImageView iv_icon;

    @ViewById(id = R.id.ll_btn_area)
    View ll_btn_area;

    private boolean isAuthorize=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();

    /*    if (!SessionManager.getInstance().getAuthUser().isAuthorized()) {
            SessionManager.getInstance().anonymousRegister(this);
        }*/
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(isAuthorize){
                    gotoMainActivity();
                }
//                if (SessionManager.getInstance().getAuthUser().isAuthorized()) {
//                    gotoMainActivity();
//                }
//                else {
//                    ll_btn_area.setVisibility(View.VISIBLE);
//                    Animation animation = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.anim_welcome_btn);
//                    ll_btn_area.startAnimation(animation);
//
//                }
            }
        }, 1000);

//        DeviceGeoLocationManager geoManager = DeviceGeoLocationManager.getInstance();
//        geoManager.requestLocation();
    }

    private void init() {
        int[] size = DensityUtil.getScreenHeightAndWidth(this);
        int imageSize = (int) (size[0] * 0.5);//width * 0.5

        ViewGroup.LayoutParams params = iv_icon.getLayoutParams();
        params.height = imageSize;
        params.width = imageSize;
        iv_icon.setLayoutParams(params);

        ll_btn_area.setVisibility(View.GONE);

    }

    private void gotoMainActivity() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_login:
//                Intent intentLogin = new Intent(this, LoginActivity.class);
//                startActivityForResult(intentLogin, 0x11);
//                break;
//            case R.id.btn_look_through:
//                showLoading();
//                SessionManager.getInstance().anonymousLogin(this);
//                break;
//        }
//    }


    @Override
    public void onRequestSucceeded(DataServiceImpl service, JSONObject data, boolean isCached) {
        super.onRequestSucceeded(service, data, isCached);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gotoMainActivity();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (SessionManager.getInstance().getAuthUser().isAuthorized()) {
            gotoMainActivity();
        }
    }
}
