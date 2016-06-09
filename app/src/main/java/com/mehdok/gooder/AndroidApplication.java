/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder;

import android.app.Application;

import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooderapilib.GooderApi;
import com.onesignal.OneSignal;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by mehdok on 4/28/2016.
 */
public class AndroidApplication extends Application implements Foreground.Listener {
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this).init();
        //OneSignal.sendTag("sajadAhmadi", "true");

        Foreground.init(this);
        Foreground.get().addListener(this);

        if (com.mehdok.gooder.BuildConfig.DEBUG) {
            // init the logger library
            Logger.init().logLevel(LogLevel.FULL);

            // init Retrofit
            GooderApi.create(Crypto.API_KEY, HttpLoggingInterceptor.Level.BODY);
        } else {
            Logger.init().logLevel(LogLevel.NONE);
            GooderApi.create(Crypto.API_KEY, HttpLoggingInterceptor.Level.NONE);
        }
    }

    @Override
    public void onBecameForeground() {
        Logger.e("onBecameForeground");
    }

    @Override
    public void onBecameBackground() {
        Logger.e("onBecameBackground");
    }
}
