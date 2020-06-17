package com.doku.android.samplesdkapps.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by dedye on 04,June,2020
 */
public class Preferences {

    private static final String KEY_CLIENT_ID = "KEY_CLIENT_ID";
    private static final String KEY_SHARED_KEY = "KEY_SHARED_KEY";
    private static final String KEY_ENVIRONMENT_SERVER = "KEY_ENVIRONMENT_SERVER";
    private static final String KEY_USE_PAGE_RESULT = "KEY_USE_PAGE_RESULT";

    private static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setClientId(Context context, String clientId){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_CLIENT_ID, clientId);
        editor.apply();
    }

    public static String getClientId(Context context){
        return getSharedPreference(context).getString(KEY_CLIENT_ID,"");
    }

    public static void setSharedKey(Context context, String sharedKey){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_SHARED_KEY, sharedKey);
        editor.apply();
    }

    public static String getSharedKey(Context context){
        return getSharedPreference(context).getString(KEY_SHARED_KEY,"");
    }

    public static void setEnvironmentServer(Context context, Boolean environmentServer){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_ENVIRONMENT_SERVER, environmentServer);
        editor.apply();
    }

    public static Boolean getEnvironmentServer(Context context){
        return getSharedPreference(context).getBoolean(KEY_ENVIRONMENT_SERVER,false);
    }

    public static void setUsePageResult(Context context, Boolean usePageResult){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_USE_PAGE_RESULT, usePageResult);
        editor.apply();
    }

    public static Boolean getUsePageResult(Context context){
        return getSharedPreference(context).getBoolean(KEY_USE_PAGE_RESULT,false);
    }

    public static void clearDataPrefs (Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(KEY_CLIENT_ID);
        editor.remove(KEY_SHARED_KEY);
        editor.remove(KEY_ENVIRONMENT_SERVER);
        editor.remove(KEY_USE_PAGE_RESULT);
        editor.apply();
    }
}
