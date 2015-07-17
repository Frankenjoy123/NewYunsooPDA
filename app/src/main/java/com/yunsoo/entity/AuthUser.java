package com.yunsoo.entity;

import com.yunsoo.util.StringHelper;

import org.json.JSONException;
import org.json.JSONObject;



public class AuthUser implements JSONEntity {

    //	private long id;
//    private String userId;
    private String token;

    private String permanent_token;

//    private String phone;
//    private String name;
//    private String imageUrl;
    private boolean isAuthorized;
//    private UserInfo userInfo;

    public AuthUser() {
    }

    public AuthUser(AuthUser user) {
//        this.userId = user.getUserId();
        this.token = user.token;
        this.permanent_token=user.permanent_token;
//        this.phone = user.getPhone();
//        if (user.getImageUrl() != null) {
//            this.imageUrl = user.getImageUrl();
//        }
//        if (user.getName() != null) {
//            this.name = user.getName();
//        }
//        this.userInfo = user.userInfo;
    }



    public String getToken() {
        return token;
    }

    public String getPermanent_token() {
        return permanent_token;
    }

    public boolean isAuthorized() {
        return !StringHelper.isStringNullOrEmpty(token) && !StringHelper.isStringNullOrEmpty(permanent_token);
    }



    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

    public void populate(JSONObject object) {
//        userId = object.optString("user_id");
        token = object.optString("token");
        permanent_token=object.optString("permanent_token");
//        phone = object.optString("phone");

    }

    public String toJsonString() throws JSONException {
        JSONObject object = new JSONObject();
//        object.put("user_id", userId);
//        object.put("phone", phone);
//        object.put("name", name);
        object.put("token", token);
        object.put("permanent_token",permanent_token);
//        if (userInfo != null) {
//            object.put("user_info", new JSONObject(userInfo.toJsonString()));
//        }
        return object.toString();
    }

//    public void setPhone(String phone) {
//        this.phone = phone;
//    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPermanent_token(String permanent_token) {
        this.permanent_token = permanent_token;
    }

    //    public void setUserId(String userId) {
//        this.userId = userId;
//    }

}
