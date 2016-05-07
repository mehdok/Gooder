/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.views;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.mehdok.singlepostviewlib.utils.TypefaceUtil;

/**
 * Created by mehdok on 4/11/2016.
 */
public class VazirEditText extends AppCompatEditText {
    public VazirEditText(Context context) {
        super(context);
        init(context);
    }

    public VazirEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VazirEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setTypeface(TypefaceUtil.getInstance().getTypeFaceForName(context, "fonts/Vazir.ttf"));
    }
}
