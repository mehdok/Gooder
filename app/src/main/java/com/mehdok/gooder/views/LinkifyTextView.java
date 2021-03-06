/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;

/**
 * Created by mehdok on 5/3/2016.
 */
public class LinkifyTextView extends AppCompatTextView {
    public LinkifyTextView(Context context) {
        super(context);
        init(context, null);
    }

    public LinkifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LinkifyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray;
        typedArray = context.obtainStyledAttributes(attrs, com.mehdok.singlepostviewlib.R.styleable.PostText);

        String fontPath = typedArray.getString(com.mehdok.singlepostviewlib.R.styleable.PostText_fontPath);
        if (fontPath == null)
            fontPath = "fonts/Vazir.ttf";
        setTypeface(com.mehdok.singlepostviewlib.utils.TypefaceUtil.getInstance().getTypeFaceForName(context, fontPath));

        typedArray.recycle();
    }

    public void setPrettyText(SpannableString str) {
        this.setMovementMethod(LinkMovementMethod.getInstance());
        super.setText(str, BufferType.SPANNABLE);
    }
}
