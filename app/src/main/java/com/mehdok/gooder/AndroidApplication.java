/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder;

import android.app.Application;

import com.mehdok.GooderApi;
import com.mehdok.gooder.crypto.Crypto;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by mehdok on 4/28/2016.
 */
public class AndroidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            // init the logger library
            Logger.init().logLevel(LogLevel.FULL);

            // init Retrofit
            GooderApi.create(Crypto.API_KEY, HttpLoggingInterceptor.Level.BODY);
        } else {
            Logger.init().logLevel(LogLevel.NONE);
            GooderApi.create(Crypto.API_KEY, HttpLoggingInterceptor.Level.NONE);
        }
    }
}
