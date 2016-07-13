package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PrefencesManager {

    private SharedPreferences mSharedPreferences;

    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY, ConstantManager.USER_MAIL_KEY, ConstantManager.USER_VK_KEY, ConstantManager.USER_GIT_KEY, ConstantManager.USER_BIO_KEY};

    public PrefencesManager() {
        this.mSharedPreferences = DevintensiveApplication.getSharedPreferences();
    }

    public void saveUserProfileData(List<String> userFields) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < userFields.size(); i++) {
            String field = userFields.get(i);
            editor.putString(USER_FIELDS[i], field);
        }
        editor.apply();
    }

    public List<String> loadUserProfileData() {
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_BIO_KEY, "null"));
        return userFields;
    }

    public void saveUserPhotos(List<Uri> uri) {
        if (uri.size() == 2) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(ConstantManager.USER_PHOTO_KEY, uri.get(0).toString());
            editor.putString(ConstantManager.AVATAR_PHOTO_KEY, uri.get(1).toString());
            editor.apply();
        } else throw new IndexOutOfBoundsException();
    }

    public List<Uri> loadUserPhotos() {
        List<Uri> photos = new ArrayList<>();
        photos.add(Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, "android.resource://com.softdesign.devintensive/drawable/userphoto")));
        photos.add(Uri.parse(mSharedPreferences.getString(ConstantManager.AVATAR_PHOTO_KEY, "android.resource://com.softdesign.devintensive/drawable/avatar")));
        return photos;
    }

    public void saveAuthToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN_KEY, authToken);
        editor.apply();
    }

    public String getAuthToken() {
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, "null");
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "null");
    }

    public void saveUserInfo(int[] info) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(ConstantManager.RATING, info[0]);
        editor.putInt(ConstantManager.CODELINES, info[1]);
        editor.putInt(ConstantManager.PROJECTS, info[2]);
        editor.apply();
    }

    public int[] loadUserInfo() {
        int[] info = new int[3];
        info[0] = mSharedPreferences.getInt(ConstantManager.RATING, '5');
        info[1] = mSharedPreferences.getInt(ConstantManager.CODELINES, '5');
        info[2] = mSharedPreferences.getInt(ConstantManager.PROJECTS, '5');
        return info;
    }

    public void saveUsernames(String[] names) {
        String[] NAMES = {ConstantManager.FIRST_NAME, ConstantManager.SECOND_NAME};
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (names.length == 2) {
            for (int i = 0; i < names.length; i++) {
                editor.putString(NAMES[i], names[i]);
            }
        }
        editor.apply();
    }

    public String[] loadUsernames() {
        String[] NAMES = {ConstantManager.FIRST_NAME, ConstantManager.SECOND_NAME};
        String[] names = new String[2];

        for (int i = 0; i < names.length; i++) {
            names[i] = mSharedPreferences.getString(NAMES[i], "null");
        }
        return names;
    }
}