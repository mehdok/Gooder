/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.views;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.mehdok.gooder.utils.TypefaceUtil;

/**
 * Created by mehdok on 4/11/2016.
 */
public class VazirButton extends AppCompatButton
{
    public VazirButton(Context context)
    {
        super(context);
        init(context);
    }

    public VazirButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public VazirButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context)
    {
        setTypeface(TypefaceUtil.getInstance().getTypeFaceForName(context, "fonts/Vazir.ttf"));
    }
}
