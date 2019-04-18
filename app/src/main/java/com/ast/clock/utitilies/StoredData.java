package com.ast.clock.utitilies;

import android.content.Context;
import android.content.SharedPreferences;


public class StoredData {


    private static final String PREFS_NAME = "AumTrust";


    private static SharedPreferences settings;

    public StoredData() {
        super();
    }

    public static void putString(Context context, String key, String text) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        settings.edit().putString(key, text).apply();
    }


    public static void putBoolean(Context context, String key, Boolean value) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        settings.edit().putBoolean(key, value).apply();
    }

    public static String getString(Context context, String key, String defaultfvalue) {
        String text;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(key, defaultfvalue);
        return text;
    }


    public static Boolean getBoolean(Context context, String key, Boolean defaultvalue) {
        Boolean value;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        value = settings.getBoolean(key, defaultvalue);
        return value;
    }

    public static void clearSharedPreference(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

    public static void putInt(Context context, String key, Integer value) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        settings.edit().putInt(key, value).apply();
    }

    public static int getInt(Context context, String key, int defValue) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defValue);
    }

    public static void putLong(Context context, String key, long value) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        settings.edit().putLong(key, value).apply();
    }

    public static long getLong(Context context, String key, long defValue) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defValue);
    }


}
