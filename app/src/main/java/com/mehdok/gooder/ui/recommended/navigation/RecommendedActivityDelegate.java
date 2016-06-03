/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.recommended.navigation;

import com.mehdok.gooder.ui.recommended.RecommendedActivity;

import java.lang.ref.WeakReference;

/**
 * Created by mehdok on 6/3/2016.
 */
public class RecommendedActivityDelegate {
    private static RecommendedActivityDelegate sInstance;

    private WeakReference<RecommendedActivity> mRef;

    private RecommendedActivityDelegate() {
    }

    public static void subscribeOn(RecommendedActivity activity) {
        sInstance = new RecommendedActivityDelegate();
        sInstance.set(activity);
    }

    public static RecommendedActivityDelegate getInstance() {
        return sInstance;
    }

    public static void unSubscribe(RecommendedActivity activity) {
        if (sInstance != null && activity != null) {
            RecommendedActivity refActivity = getInstance().mRef.get();
            if (!activity.equals(refActivity)) {
                return;
            }
            sInstance.destroy();
            sInstance = null;
        }
    }

    private void set(RecommendedActivity activity) {
        mRef = new WeakReference<>(activity);
    }

    private void destroy() {
        if (mRef != null) {
            mRef.clear();
        }
    }

    public RecommendedActivity getActivity() {
        return getInstance().mRef.get();
    }
}
