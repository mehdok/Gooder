/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GooderApi {
    private static GooderApi instance = null;
    public static String apiKey;

    // access code expire time is 20 min since last usage, minus 5 for extra safety
    public static final long ACCESS_CODE_LIMIT = (20 - 5) * 60 * 1000;

    public static String mAccessCode;
    public static long lastOperation = 0;

    private static final String BASE_API_URL = "http://gooder.in/api/";
    private final GooderInterface gooderApi;

    public static GooderApi getInstance() {
        if (instance == null) {
            throw new RuntimeException("GooderApi.create has not called yet.");
        }
        return instance;
    }

    public GooderInterface getApi() {
        return gooderApi;
    }

    public static synchronized void create(String apiKey, HttpLoggingInterceptor.Level logLevel) {
        if (instance == null) {
            instance = new GooderApi(apiKey, logLevel);
        }
    }

    private GooderApi(String apiKey, HttpLoggingInterceptor.Level logLevel) {
        GooderApi.apiKey = apiKey;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(logLevel);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        gooderApi = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(GooderInterface.class);

    }
}
