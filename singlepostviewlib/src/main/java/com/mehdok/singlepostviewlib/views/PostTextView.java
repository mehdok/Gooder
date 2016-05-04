/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;

import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.utils.TypefaceUtil;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostTextView extends AppCompatTextView {
    public PostTextView(Context context) {
        super(context);
        init(context, null);
    }

    public PostTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PostTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public void setPrettyText(SpannableString str) {
        this.setMovementMethod(LinkMovementMethod.getInstance());
        super.setText(str, BufferType.SPANNABLE);
    }
}
