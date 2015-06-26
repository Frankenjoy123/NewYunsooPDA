package com.itxiaowu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itxiaowu.annotation.ViewById;
import com.itxiaowu.dialog.LoadingDialog;
import com.itxiaowu.exception.BaseException;
import com.itxiaowu.exception.ServerAuthException;
import com.itxiaowu.service.DataServiceImpl;

import org.json.JSONObject;

import java.lang.reflect.Field;




public abstract class BaseActivity extends Activity implements DataServiceImpl.DataServiceDelegate {

    protected boolean isLoading;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        try {
            autowireViewForClass(this, this.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadingDialog = new LoadingDialog(this);
    }


    public void showLoading() {
        isLoading = true;
        loadingDialog.setCancelable(true);
        loadingDialog.show();
    }

    public void showLoading(DataServiceImpl service) {
        isLoading = true;
        loadingDialog.setCancelable(true);
        loadingDialog.setService(service);
        loadingDialog.show();
    }


    public void hideLoading() {
        isLoading = false;
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }


    private void autowireViewForClass(Activity activity, Class<? extends BaseActivity> clazz) throws Exception {

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ViewById.class)) {
                continue;
            }
            if (!View.class.isAssignableFrom(field.getType())) {
                continue;
            }
            ViewById viewId = field.getAnnotation(ViewById.class);
            int resId = viewId.id();


            if (resId == 0) {
                String resStringId = field
                        .getName();
                resId = activity.getResources().getIdentifier(resStringId, "id", activity.getPackageName());
            }
            View view = activity.findViewById(resId);
            if (view == null) {
                throw new RuntimeException("No view resource with the id of " + resId + " found. The required field " + field.getName() + " could not be autowired.");
            }
            field.setAccessible(true);
            field.set(activity, view);
        }
    }

    @Override
    public void onRequestSucceeded(DataServiceImpl service, JSONObject data, boolean isCached) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoading();

            }
        });
    }

    @Override
    public void onRequestFailed(DataServiceImpl service, final BaseException exception) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                hideLoading();
//                if (exception instanceof ServerAuthException) {
//                    ServerAuthException authException = (ServerAuthException) exception;
//                    if (authException.isLoginRequired()) {
//                        if (SessionManager.getInstance().getAuthUser().isAuthorized() && !SessionManager.getInstance().getAuthUser().isAnonymous()) {
//                            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                        } else {
//                            //renew anonymous user token
//                            SessionManager.getInstance().logout();
//                            Intent intent = new Intent(BaseActivity.this, WelcomeActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            BaseActivity.this.finish();
//                        }
//                    } else {
//                        ToastMessageHelper.showErrorMessage(BaseActivity.this, R.string.error_incorrect_password, false);
//                    }
//                } else {
//                    ToastMessageHelper.showErrorMessage(BaseActivity.this, exception.getMessage(), false);
//                }
//            }
//        });
    }


}
