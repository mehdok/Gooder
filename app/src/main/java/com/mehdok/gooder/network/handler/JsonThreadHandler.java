/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network.handler;

import android.os.Handler;
import android.os.Message;

import com.mehdok.gooder.network.JsonHandler;

import java.lang.ref.WeakReference;

/**
 * Created by mehdok on 4/11/2016.
 */
public class JsonThreadHandler extends Handler
{
    private final WeakReference<JsonHandler> mActivity;

    public JsonThreadHandler(JsonHandler activity)
    {
        mActivity = new WeakReference<JsonHandler>(activity);
    }

    @Override
    public void handleMessage(Message msg)
    {
        JsonHandler activity = mActivity.get();
        if (activity != null)
        {
            activity.handleMessage(msg);
        }
    }
}
