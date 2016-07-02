package com.softdesign.devintensive.data.managers;

/**
 * Created by 1 on 27.06.2016.
 */
public class DataManager {
    private static DataManager INSTANCE = null;
    private PrefencesManager mPrefencesManager;

    public DataManager() {
        this.mPrefencesManager = new PrefencesManager();
    }

    public static DataManager getINSTANCE() {
        if(INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public PrefencesManager getPrefencesManager() {
        return mPrefencesManager;
    }
}
