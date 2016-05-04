/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.LruCache;

/**
 * Created by mehdok on 4/11/2016.
 */
public class TypefaceUtil {
    LruCache<String, Typeface> typefaceLruCache;
    private static TypefaceUtil Instance;

    public static TypefaceUtil getInstance() {
        if (Instance == null) {
            Instance = new TypefaceUtil();
        }

        return Instance;
    }

    private TypefaceUtil() {
        typefaceLruCache = new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 1024 / 20));
    }

    public Typeface getTypeFaceForName(Context context, String fontName) {
        if (typefaceLruCache.get(fontName) != null) {
            return typefaceLruCache.get(fontName);
        }
        final Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
        if (typeface != null) {
            typefaceLruCache.put(fontName, typeface);
        }
        return typeface;
    }
}
