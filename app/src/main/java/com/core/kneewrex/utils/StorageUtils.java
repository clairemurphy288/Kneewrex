package com.core.kneewrex.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StorageUtils {

    public static void saveDataInPreferences(Context c, String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(c).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveDataInPreferences(Context c, String key, int value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(c).edit();
        editor.putInt(key, value);
        editor.apply();
    }


    public static void saveDataInPreferences(Context c, String key, boolean value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(c).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void saveDataInPreferences(Context c, String key, long value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(c).edit();
        editor.putLong(key, value);
        editor.apply();
    }


    public static boolean getDataFromPreferences(Context c, String key, boolean defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        return preferences.getBoolean(key, defaultValue);
    }

    public static long getDataFromPreferences(Context c, String key, long defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        return preferences.getLong(key, defaultValue);
    }

    public static String getDataFromPreferences(Context c, String key, String defaultValue) {
        if (c == null)
            return defaultValue;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        return preferences.getString(key, defaultValue);
    }

    public static int getDataFromPreferences(Context c, String key, int defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        return preferences.getInt(key, defaultValue);
    }

    public static void clearAllPreferences(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }
}
