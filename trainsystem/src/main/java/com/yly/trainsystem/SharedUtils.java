package com.yly.trainsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.Objects;

public class SharedUtils {

    private static SharedUtils utils = new SharedUtils();
    private static SharedPreferences preferences;

    public static SharedUtils getInstance(Context context) {
        preferences = context.getSharedPreferences("share_file", Context.MODE_PRIVATE);
        return utils;
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public String  getString(String key) {
       return preferences.getString(key, null);
    }

    public int getInt(String key) {
       return  preferences.getInt(key, -1);
    }

    public static String getUserId(Context context) {
        int id = SharedUtils.getInstance(Objects.requireNonNull(context)).getInt("userId");
        if (-1 == id) {
            Toast.makeText(context, R.string.user_id_empty, Toast.LENGTH_LONG).show();
        }
        return   String.valueOf(id);
    }

}
