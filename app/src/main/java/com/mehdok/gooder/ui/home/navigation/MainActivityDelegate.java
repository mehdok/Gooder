/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.navigation;

import com.mehdok.gooder.ui.home.MainActivity;

import java.lang.ref.WeakReference;

/**
 * Created by mehdok on 4/13/2016.
 */
public class MainActivityDelegate
{
    private static MainActivityDelegate sInstance;

    private WeakReference<MainActivity> mRef;

    private MainActivityDelegate()
    {
    }

    public static void subscribeOn(MainActivity activity)
    {
        sInstance = new MainActivityDelegate();
        sInstance.set(activity);
    }

    public static MainActivityDelegate getInstance()
    {
        return sInstance;
    }

    public static void unSubscribe(MainActivity activity)
    {
        if (sInstance != null && activity != null)
        {
            MainActivity refActivity = getInstance().mRef.get();
            if (!activity.equals(refActivity))
                return;
            sInstance.destroy();
            sInstance = null;
        }
    }

    private void set(MainActivity activity)
    {
        mRef = new WeakReference<>(activity);
    }

    private void destroy()
    {
        if (mRef != null)
            mRef.clear();
    }

    public MainActivity getActivity()
    {
        return getInstance().mRef.get();
    }
}
