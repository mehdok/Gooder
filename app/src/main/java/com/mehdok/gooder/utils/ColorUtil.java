/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.utils;

import android.graphics.Color;

/**
 * Created by mehdok on 6/9/2016.
 */
public class ColorUtil {
    private static final float REDUCE = 0.05f;

    public static int getDarkerColor(int color, int factor) {
        float[] hsv = new float[3];
        float multiplayer = factor * REDUCE;
        Color.colorToHSV(color, hsv);
        hsv[2] -= multiplayer;

        if (hsv[2] < 0.6) {
            hsv[2] = 0.6f;
        }

        color = Color.HSVToColor(hsv);
        return color;
    }
}
