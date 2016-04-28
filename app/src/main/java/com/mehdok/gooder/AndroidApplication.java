/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by mehdok on 4/28/2016.
 */
public class AndroidApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        // init the logger library
        if (BuildConfig.DEBUG)
            Logger.init().logLevel(LogLevel.FULL);
        else
            Logger.init().logLevel(LogLevel.NONE);
    }
}
