package com.softdesign.devintensive.data.managers;

import android.content.Context;
import android.util.Log;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class DataManager {
    private static DataManager INSTANCE = null;

    private Context mContext;
    private PrefencesManager mPrefencesManager;
    private RestService mRestService;

    public DataManager() {
        try {
            this.mPrefencesManager = new PrefencesManager();
            this.mContext = DevintensiveApplication.getContext();
            this.mRestService = new ServiceGenerator().createService(RestService.class);
        }catch (Exception e) {
            Log.d("DEV ", e.toString());
        }
    }

    public static DataManager getInstance() {
        try {
        if(INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        }catch (Exception e) {
            Log.d("DEV ", e.toString());
        }
        return INSTANCE;
    }

    public PrefencesManager getPrefencesManager() {
        return mPrefencesManager;
    }

    public Context getContext() {
        return mContext;
    }

    //region ==========Network=========
    public retrofit2.Call<UserModelRes> loginUser(UserLoginReq userLoginReq) {
        List<String> userInfoData = new ArrayList<String>();
        return mRestService.loginUser(userLoginReq);
    }

    public Call<UserListRes> getUserList() {
        return mRestService.getUserList();
    }
    //endregion
    //region ==========Database=========
    //endregion
}
