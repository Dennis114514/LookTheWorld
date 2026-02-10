package com.dennis114514.looktheworld.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String PREF_NAME = "app_settings";
    
    private SharedPreferences sharedPreferences;
    
    public SettingsManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}