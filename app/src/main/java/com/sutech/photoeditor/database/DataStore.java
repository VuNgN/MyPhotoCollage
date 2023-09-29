package com.sutech.photoeditor.database;

import android.content.Context;

import com.sutech.photoeditor.widget.text.database.MySharedPreferences;


public class DataStore {

    protected static DataStore instance;
    protected MySharedPreferences sharedPreferences;
    protected DbConnection dbConnection;

    private static final String KEY_PREF_IS_FIRST_OPEN = "KEY_PREF_IS_FIRST_OPEN";
    private static final String KEY_PREF_THEME_APP = "KEY_PREF_THEME_APP";
    private static final String KEY_PREF_PASSWORD = "KEY_PREF_PASSWORD";
    private static final String KEY_PREF_USE_PASSWORD = "KEY_PREF_USE_PASSWORD";

    /**
     * Call when start application
     */
    public static void init(Context context) {
        instance = new DataStore();
        instance.sharedPreferences = new MySharedPreferences(context);
//        instance.dbConnection = new DbConnection(context);
    }

    public static DataStore getInstance() {
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }


    public static void savePassword(String password) {
        getInstance().sharedPreferences.putStringValue(KEY_PREF_PASSWORD, password);
    }

    public static String getPassword() {
        return getInstance().sharedPreferences.getStringValue(KEY_PREF_PASSWORD);
    }

    public static void saveTheme(Integer themeId) {
        getInstance().sharedPreferences.putIntValue(KEY_PREF_THEME_APP, themeId);
    }

    public static Integer getTheme() {
        return getInstance().sharedPreferences.getIntValue(KEY_PREF_THEME_APP);
    }

    public static void setUsePassword(Boolean password) {
        getInstance().sharedPreferences.putBooleanValue(KEY_PREF_USE_PASSWORD, password);
    }

    public static Boolean getUsePassword() {
        return getInstance().sharedPreferences.getBooleanValue(KEY_PREF_USE_PASSWORD);
    }

    public static Boolean checkPassword(String password) {
        return password.equals(getInstance().sharedPreferences.getStringValue(KEY_PREF_PASSWORD));
    }
    public static Boolean checkNotFirstOpen() {
        return getInstance().sharedPreferences.getBooleanValue(KEY_PREF_IS_FIRST_OPEN);
    }

    public static void setNotFirstOpen(boolean isFirstOpen) {
          getInstance().sharedPreferences.putBooleanValue(KEY_PREF_IS_FIRST_OPEN,isFirstOpen);
    }
//
//    public static void saveNote(NoteObj noteObj) {
//        getInstance().dbConnection.saveDataNote(noteObj);
//    }
//
//    public static void saveImage(ImageGalleryObj imageGalleryObj){
//        getInstance().dbConnection.saveImage(imageGalleryObj);
//    }
//
//    public static void saveSound(SoundObj soundObj){
//        getInstance().dbConnection.saveSound(soundObj);
//    }


}
