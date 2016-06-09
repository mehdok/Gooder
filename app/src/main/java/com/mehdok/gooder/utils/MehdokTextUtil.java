/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.utils;

/**
 * Created by mehdok on 6/9/2016.
 */
public class MehdokTextUtil {
    public static final int BODY_COUNT = 200;

    public static String getLimitedText(String str, int textLimit) {
        if (str == null) return null;

        if (str.length() <= textLimit) {
            return str;
        } else {
            return str.substring(0, textLimit) + "<br/>&#x25BC;";
        }
    }
}
