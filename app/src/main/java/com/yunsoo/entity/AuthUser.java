package com.yunsoo.entity;

import com.yunsoo.util.StringHelper;

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



    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

    public void populate(JSONObject object) {
        userId = object.optString("user_id");
        token = object.optString("t");
        phone = object.optString("phone");

    }

    public String toJsonString() throws JSONException {
        JSONObject object = new JSONObject();
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

}
