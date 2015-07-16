package com.yunsoo.service;

import android.os.Environment;

import com.yunsoo.entity.AuthUser;
import com.yunsoo.exception.LocalGeneralException;
import com.yunsoo.exception.NetworkNotAvailableException;
import com.yunsoo.exception.ServerAuthException;
import com.yunsoo.exception.ServerGeneralException;
import com.yunsoo.manager.SessionManager;
import com.yunsoo.network.RequestManager;
import com.yunsoo.util.Constants;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;


public class FileUpLoadService extends DataServiceImpl {

    private static final String UPLOAD_URL = "/package/file";
    private String filePath;


    public FileUpLoadService(String filePath)
    {
        this.filePath = filePath;
    }

    @Override
    protected JSONObject method() throws ServerAuthException, ServerGeneralException, LocalGeneralException,
            NetworkNotAvailableException, Exception {
        RequestManager.PostByFile(UPLOAD_URL,filePath);
        return null;
    }
}
