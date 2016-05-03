/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;

import com.mehdok.gooder.utils.TypefaceUtil;

/**
 * Created by mehdok on 5/3/2016.
 */
public class LinkifyTextView extends AppCompatTextView {
    public LinkifyTextView(Context context) {
        super(context);
        init(context);
    }

    public LinkifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LinkifyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setTypeface(TypefaceUtil.getInstance().getTypeFaceForName(context, "fonts/Vazir.ttf"));
    }

    public void setPrettyText(SpannableString str) {
        this.setMovementMethod(LinkMovementMethod.getInstance());
        super.setText(str, BufferType.SPANNABLE);
    }
}
