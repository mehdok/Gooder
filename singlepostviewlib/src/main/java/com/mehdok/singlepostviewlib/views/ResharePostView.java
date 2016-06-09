/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.models.PostBody;
import com.mehdok.singlepostviewlib.utils.TimeUtil;

/**
 * Created by mehdok on 6/8/2016.
 */
public class ResharePostView extends LinearLayout {

    private View rootLayout;
    private PostTextView tvDate;
    private PostTextView tvshareCount;
    private PostTextView tvLikeCount;
    private PostTextView tvAuthor;
    private PostTextView tvTitle;
    private PostBodyView bodyView;
    private String reshares;
    private String likes;

    private String mDate;
    private String mShareCount;
    private String mLikeCount;
    private String mAuthor;
    private String mTitle;
    private String mBody;

    public ResharePostView(Context context, String date, String shareCount, String likeCount,
                           String author,
                           String title, String body) {
        super(context);

        mDate = date;
        mShareCount = shareCount;
        mLikeCount = likeCount;
        mAuthor = author;
        mTitle = title;
        mBody = body;

        init(context);
    }

    public ResharePostView(Context context) {
        super(context);
        init(context);
    }

    public ResharePostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ResharePostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.reshare_view, this);

        reshares = context.getString(R.string.reshare);
        likes = context.getString(R.string.like);

        rootLayout = findViewById(R.id.reshare_root_layout);
        tvDate = (PostTextView) findViewById(R.id.reshare_date);
        tvshareCount = (PostTextView) findViewById(R.id.reshare_count);
        tvLikeCount = (PostTextView) findViewById(R.id.reshare_like_count);
        tvAuthor = (PostTextView) findViewById(R.id.reshare_author_name);
        tvTitle = (PostTextView) findViewById(R.id.reshare_title);
        bodyView = (PostBodyView) findViewById(R.id.reshare_post_body);

        setData(mDate, mShareCount, mLikeCount, mAuthor, mTitle, mBody);
    }


    public void setData(String date, String shareCount, String likeCount, String author,
                        String title, String body) {
        tvDate.setText(TimeUtil.getInstance().getReadableDate(date));
        tvshareCount.setText(String.format(reshares, shareCount));
        tvLikeCount.setText(String.format(likes, likeCount));
        tvAuthor.setText(author);

        if (title == null || title.isEmpty()) {
            tvTitle.setVisibility(GONE);
        } else {
            tvTitle.setText(title);
        }

        bodyView.setPostBody(new PostBody(body, null, null));
        //TODO set proper tag listener
        //TODO set proper click listener to open reshared post
    }

    public void changeBackgroundColor(int color) {
        rootLayout.setBackgroundColor(color);
    }
}
