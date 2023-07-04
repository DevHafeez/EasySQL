package com.hafeez.easysql;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class EasyCache {

    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    public static void storeInt(Activity activity, String key, int value) {
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Activity activity, String key, int defaultValue) {
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        return preferences.getInt(key, defaultValue);
    }

    public static void storeString(Activity activity, String key, String value) {
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Activity activity, String key, String defaultValue) {
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    public static void storeBoolean(Activity activity, String key, boolean value) {
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getBoolean(Activity activity, String key, boolean defaultValue) {
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defaultValue);
    }

    public static void storeObject(Activity activity, String key, Object object) {
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        editor.putString(key, json);
        editor.apply();
    }

    public static <T> T getObject(Activity activity, String key, Class<T> className, T defaultObject) {
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(key, null);
        if (json != null) {
            return gson.fromJson(json, className);
        }
        return defaultObject;
    }

}
