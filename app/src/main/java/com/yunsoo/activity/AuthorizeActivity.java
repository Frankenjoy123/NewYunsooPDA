package com.yunsoo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.yunsoo.entity.AuthUser;
import com.yunsoo.entity.AuthorizeRequest;
import com.yunsoo.entity.LoginResult;
import com.yunsoo.entity.ScanAuthorizeInfo;
import com.yunsoo.exception.BaseException;
import com.yunsoo.manager.DeviceManager;
import com.yunsoo.manager.SessionManager;
import com.yunsoo.network.RequestManager;
import com.yunsoo.network.RestClient;
import com.yunsoo.service.AuthLoginService;
import com.yunsoo.service.AuthorizeService;
import com.yunsoo.service.DataServiceImpl;
import com.yunsoo.service.LogisticActionService;
import com.yunsoo.util.ToastMessageHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthorizeActivity extends BaseActivity implements DataServiceImpl.DataServiceDelegate{

    private EditText et_authorize_code;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        getActionBar().hide();
        et_authorize_code= (EditText) findViewById(R.id.et_authorize_code);
        bindScanAuthorize();
    }

    private void bindScanAuthorize() {
        et_authorize_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content=s.toString();
                try {
                    //start AuthLoginService
                    JSONObject object=new JSONObject(content);
                    String token=object.optString("t");
//                    AuthUser user = new AuthUser();
//                    user.setToken(token);
//                    SessionManager.getInstance().saveLoginCredential(user);

                    AuthLoginService authLoginService=new AuthLoginService(token);
                    authLoginService.setDelegate(AuthorizeActivity.this);
                    authLoginService.start();
                    showLoading();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestSucceeded(final DataServiceImpl service, final JSONObject data, boolean isCached) {
        Log.d("ZXW","register successfully");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(service instanceof AuthLoginService){
                    LoginResult loginResult=new LoginResult();
                    loginResult.populate(data);
                    String accessToken=loginResult.getAccessToken();
                    String permanentToken=loginResult.getPermanentToken();
                    AuthUser user = new AuthUser();
                    user.setToken(accessToken);
                    user.setPermanent_token(permanentToken);

                    SessionManager.getInstance().saveLoginCredential(user);

                    try {
                        JSONObject object=new JSONObject(content);

                        //set authorize request
                        ScanAuthorizeInfo scanAuthorizeInfo=new ScanAuthorizeInfo();
                        scanAuthorizeInfo.populate(object);
                        AuthorizeRequest request=new AuthorizeRequest();
                        request.setAccountId(scanAuthorizeInfo.getAccountId());
                        request.setComments(scanAuthorizeInfo.getDeviceComments());
                        DeviceManager.initializeIntance(AuthorizeActivity.this);
                        DeviceManager deviceManager=DeviceManager.getInstance();
                        request.setDeviceCode(deviceManager.getDeviceId());
                        request.setDeviceName(scanAuthorizeInfo.getDeviceName());
                        request.setOs("Android");

                        //start device register service
                        AuthorizeService service=new AuthorizeService(request);
                        service.setDelegate(AuthorizeActivity.this);
                        service.start();

                        LogisticActionService actionService=new LogisticActionService();
                        actionService.start();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                if(service instanceof AuthorizeService){
                    SharedPreferences preferences=getSharedPreferences("yunsoo_pda",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("isAuthorize",true);
                    editor.commit();
                    hideLoading();
                    ToastMessageHelper.showMessage(AuthorizeActivity.this,R.string.scan_success,true);

                    Intent intent=new Intent(AuthorizeActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }

    @Override
    public void onRequestFailed(DataServiceImpl service, BaseException exception) {
        Log.d("ZXW","register failed");
        ToastMessageHelper.showErrorMessage(AuthorizeActivity.this,R.string.authorize_failed,true);
        super.onRequestFailed(service,exception);
    }
}
