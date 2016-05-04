/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.content.Context;
import android.os.Build;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.models.PostBody;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostBodyView extends LinearLayout {
    private PostTextView tvBody;
    private PostTextView tvNote;

    public PostBodyView(Context context) {
        super(context);
        init(context);
    }

    public PostBodyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostBodyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.post_body, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvBody = (PostTextView) findViewById(R.id.body_text);
        tvNote = (PostTextView) findViewById(R.id.note_text);
    }

    public void setPostBody(PostBody postBody) {
        if (postBody.getNote() == null) {
            tvNote.setPrettyText(postBody.getBody());
            tvBody.setVisibility(View.GONE);
        } else {
            tvNote.setPrettyText(postBody.getNote());

            BackgroundColorSpan backgroundColorSpan;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                backgroundColorSpan = new BackgroundColorSpan(getResources()
                        .getColor(R.color.shared_background_color, null));
            } else {
                backgroundColorSpan = new BackgroundColorSpan(getResources()
                        .getColor(R.color.shared_background_color));
            }

            postBody.getBody().setSpan(backgroundColorSpan, 0, postBody.getBody().length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
