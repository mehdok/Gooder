/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.mehdok.singlepostviewlib.utils.TypefaceUtil;

/**
 * Created by mehdok on 4/11/2016.
 */
public class VazirTextView extends AppCompatTextView
{
    public VazirTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    public VazirTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public VazirTextView(Context context)
    {
        super(context);
        init(context);
    }

    private void init(Context context)
    {
        setTypeface(TypefaceUtil.getInstance().getTypeFaceForName(context, "fonts/Vazir.ttf"));
    }
}
