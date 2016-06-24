/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.utils.TypefaceUtil;

/**
 * @author mehdok on 5/4/2016.
 */
public class PostEditText extends AppCompatEditText {
    public PostEditText(Context context) {
        super(context);
        init(context, null);
    }

    public PostEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PostEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.PostText);

        String fontPath = typedArray.getString(R.styleable.PostText_fontPath);
        if (fontPath == null) {
            fontPath = "fonts/Vazir.ttf";
        }
        setTypeface(TypefaceUtil.getInstance().getTypeFaceForName(context, fontPath));

        typedArray.recycle();
    }
}
