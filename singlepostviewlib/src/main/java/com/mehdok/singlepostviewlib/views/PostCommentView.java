/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.mehdok.singlepostviewlib.R;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostCommentView extends RelativeLayout {

    private PostTextView tvAuthor;
    private PostTextView tvDate;
    private PostTextView tvBody;

    public PostCommentView(Context context) {
        super(context);
        init(context);
    }

    public PostCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostCommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.post_comment, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvAuthor = (PostTextView) findViewById(R.id.comment_author);
        tvDate = (PostTextView) findViewById(R.id.comment_date);
        tvBody = (PostTextView) findViewById(R.id.comment_body);
    }

    public void setComment(String author, String date, SpannableString body) {
        tvAuthor.setText(author);
        tvDate.setText(date);
        tvBody.setPrettyText(body);
    }
}
