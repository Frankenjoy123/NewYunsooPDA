package com.itxiaowu.service;

import org.json.JSONObject;

import com.itxiaowu.entity.AuthUser;
import com.itxiaowu.exception.LocalGeneralException;
import com.itxiaowu.exception.NetworkNotAvailableException;
import com.itxiaowu.exception.ServerAuthException;
import com.itxiaowu.exception.ServerGeneralException;
import com.itxiaowu.manager.SessionManager;
import com.itxiaowu.network.RequestManager;

public class LoginService extends DataServiceImpl {

    private static final String LOGIN_URL = "/auth/login";
    private String phone;

    private String deviceCode;


    public LoginService(String phone, String deviceCode) {
        this.phone = phone;
        this.deviceCode = deviceCode;
    }

    @Override
    protected JSONObject method() throws ServerAuthException, ServerGeneralException, LocalGeneralException,
            NetworkNotAvailableException, Exception {

        JSONObject object = new JSONObject();
        object.put("cellular", this.phone);
        object.put("device_code", deviceCode);

        JSONObject result = RequestManager.Post(LOGIN_URL, object.toString());
        AuthUser user = new AuthUser();
        user.populate(result);
        SessionManager.getInstance().saveLoginCredential(user);

        JSONObject userInfoJsonObject = RequestManager.Get("/user/" + user.getUserId(), null);
//        UserInfo userInfo = new UserInfo();
//        userInfo.populate(userInfoJsonObject);
//        user.setUserInfo(userInfo);
        SessionManager.getInstance().saveLoginCredential(user);

        return result;

    }

}
