package com.xll.xinsheng.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xll.xinsheng.bean.InitialData;
import com.xll.xinsheng.bean.LoginInfo;

import java.lang.reflect.Type;

public class Cache<T> {

    private static final String TAG = "Cache";

    public static final String INITIAL_DATA = "INITIAL_DATA";

    public static final String LOGIN_INFO = "LOGIN_INFO";

    public static final String GENERAL_INFO = "GENERAL_INFO";

    public static final String KEY_SAVE_LOGIN = "save_login_info";

    public static final String KEY_USERNAME = "key_username";

    public static final String KEY_PASSWORD = "key_password";

    public static final String KEY_NOTICE = "key_notice";


    private SharedPreferences sharedPreferences;

    public Cache(@NonNull Context context, String fileName) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public void clearCache(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public LoginInfo getLoginInfo() {
        String content = sharedPreferences.getString("content", null);
        Gson gson = new Gson();
        LoginInfo loginInfo = gson.fromJson(content, LoginInfo.class);
        return loginInfo;

    }

    public  InitialData getInitialData() {
        String content = sharedPreferences.getString("content", null);
        Log.i(TAG,  "content:" + content);
        Gson gson = new Gson();
        return gson.fromJson(content, InitialData.class);
    }

    public T getInfo() {
        String content = sharedPreferences.getString("content", null);
        Log.i(TAG,  "content:" + content);
        Gson gson = new Gson();
        Type type = new TypeToken<T>() {}.getType();
        return gson.fromJson(content, type);
    }

    public void saveGeneralInfo(String key, Boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public void saveGeneralInfo(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public boolean getBoolean(String key) {
      return  sharedPreferences.getBoolean(key, false);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public String getStrInfo() {
        return sharedPreferences.getString("content", null);
    }


    public void saveInfo(String info) {
        sharedPreferences.edit().putString("content", info).apply();
        sharedPreferences.edit().putLong("time", System.currentTimeMillis()).apply();
    }

    public boolean isExpired(long expireTime, long currentTime) {
        long time = sharedPreferences.getLong("time", 0);
        if (time == 0) {
            return true;
        }
        return time - currentTime > expireTime;

    }

    static class CacheInfo<T> {

        private long time;

        private T info;

        public CacheInfo(long time, T info) {
            this.time = time;
            this.info = info;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public T getInfo() {
            return info;
        }

        public void setInfo(T info) {
            this.info = info;
        }
    }

}
