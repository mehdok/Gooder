/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mehdok on 4/10/2016.
 */
public class PreferencesManager {
    private SharedPreferences sharedPreferences;

    public PreferencesManager(Context ctx) {
        sharedPreferences =
                ctx.getSharedPreferences(PreferencesKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean isFirstRun() {
        return sharedPreferences.getBoolean(PreferencesKey.PREF_FIRST_RUN, true);
    }

    public void setFirstRun(boolean firstRun) {
        sharedPreferences.edit().putBoolean(PreferencesKey.PREF_FIRST_RUN, firstRun).apply();
    }
}
