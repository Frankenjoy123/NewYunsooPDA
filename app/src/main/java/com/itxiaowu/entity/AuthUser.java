package com.itxiaowu.entity;

import com.itxiaowu.util.StringHelper;

import org.json.JSONException;
import org.json.JSONObject;



public class AuthUser implements JSONEntity {

    //	private long id;
    private String userId;
    private String token;

    private String phone;
    private String name;
    private String imageUrl;
    private boolean isAuthorized;
//    private UserInfo userInfo;

    public AuthUser() {
    }

    public AuthUser(AuthUser user) {
        this.userId = user.getUserId();
        this.token = user.token;
        this.phone = user.getPhone();
        if (user.getImageUrl() != null) {
            this.imageUrl = user.getImageUrl();
        }
        if (user.getName() != null) {
            this.name = user.getName();
        }
//        this.userInfo = user.userInfo;
    }

//	public long getId() {
//		return id;
//	}


    public String getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getToken() {
        return token;
    }

    public boolean isAuthorized() {
        return !StringHelper.isStringNullOrEmpty(token) && !StringHelper.isStringNullOrEmpty(userId);
    }

//userId    public boolean isAnonymous() {
//        return this.userInfo == null || this.userInfo.getCellular() == null || this.userInfo.getCellular().length() == 0;
//    }

    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

    public void populate(JSONObject object) {
        userId = object.optString("user_id");
        token = object.optString("token");
        phone = object.optString("phone");
//        JSONObject userInfoObject = object.optJSONObject("user_info");
//user_info        if (userInfoObject != null) {
//            userInfo = new UserInfo();
//            userInfo.populate(userInfoObject);
//        }
    }

    public String toJsonString() throws JSONException {
        JSONObject object = new JSONObject();
//		object.put("id", id);
        object.put("user_id", userId);
        object.put("phone", phone);
        object.put("name", name);
        object.put("token", token);
//        if (userInfo != null) {
//            object.put("user_info", new JSONObject(userInfo.toJsonString()));
//        }
        return object.toString();
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public void setUserInfo(UserInfo userInfo) {
//        this.userInfo = userInfo;
//    }
//
//    public UserInfo getUserInfo() {
//        return userInfo;
//    }
}
