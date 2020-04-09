package com.infolai.liquorder.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
    private static final String SHARED_PREFERENCE = "SharedPreference";
    private static SharedPreferenceUtil sharedPreferenceUtil;
//    private Context context;
//    private SharedPreferences pref;
//    SharedPreferences.Editor editor;

    public static SharedPreferenceUtil getInstance() {
        if (sharedPreferenceUtil == null) {
            sharedPreferenceUtil = new SharedPreferenceUtil();
        }

        return sharedPreferenceUtil;
    }

    public String getDate(Context context, String field) {
        return context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE).getString(field, "");
    }

    public void saveData(Context context, String field, String value) {
        SharedPreferences.Editor editor =  context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();

        editor.putString(field, value);
        editor.apply();
    }


    public void removeData(Context context, String field) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();

        editor.remove(field);
        editor.apply();

    }
}
