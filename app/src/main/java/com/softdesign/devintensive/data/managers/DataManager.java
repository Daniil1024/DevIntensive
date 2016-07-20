package com.softdesign.devintensive.data.managers;

import android.content.Context;
import android.util.Log;

import com.softdesign.devintensive.data.network.PicassoCache;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UploadPhotoRes;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;

public class DataManager {
    private static DataManager INSTANCE = null;
    private Picasso mPicasso = null;

    private Context mContext;
    private PrefencesManager mPrefencesManager;
    private RestService mRestService;

    private DaoSession mDaoSession;

    public DataManager() {
        try {
            this.mPrefencesManager = new PrefencesManager();
            this.mContext = DevintensiveApplication.getContext();
            this.mRestService = new ServiceGenerator().createService(RestService.class);
            this.mPicasso = new PicassoCache(mContext).getPicassoInstance();
            this.mDaoSession = DevintensiveApplication.getDaoSession();
        } catch (Exception e) {
            Log.d("DEV ", e.toString());
        }
    }

    public static DataManager getInstance() {
        try {
            if (INSTANCE == null) {
                INSTANCE = new DataManager();
            }
        } catch (Exception e) {
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

    public Picasso getPicasso() {
        return mPicasso;
    }

    //region ==========Network=========
    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq) {
        List<String> userInfoData = new ArrayList<String>();
        return mRestService.loginUser(userLoginReq);
    }

    public Call<UploadPhotoRes> uploadPhoto(String userId, RequestBody photoFile) {
        return mRestService.uploadPhoto(userId, photoFile);
    }

    public Call<UserListRes> getUserListFromNetwork() {
        return mRestService.getUserList();
    }
    //endregion

    //region ==========Database=========


    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public List<User> getUserListFromDb() {
        List<User> userList = new ArrayList<>();
        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.CodeLines.gt(0))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public List<User> getUserListByName(String query) {

        List<User> userList = new ArrayList<>();
        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.Rating.gt(0), UserDao.Properties.SearchName.like("%" + query.toUpperCase() + "%"))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }
    //endregion
}
