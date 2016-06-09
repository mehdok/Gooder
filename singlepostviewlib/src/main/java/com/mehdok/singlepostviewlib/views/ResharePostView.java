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
import com.mehdok.singlepostviewlib.interfaces.ReshareBodyClickListener;
import com.mehdok.singlepostviewlib.interfaces.UserProfileClickListener;
import com.mehdok.singlepostviewlib.models.PostBody;
import com.mehdok.singlepostviewlib.utils.PrettySpann;
import com.mehdok.singlepostviewlib.utils.TimeUtil;

/**
 * Created by mehdok on 6/8/2016.
 */
public class ResharePostView extends LinearLayout implements View.OnClickListener {

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
    private String mAuthorId;
    private String mTitle;
    private String mBody;
    private String mPostId;
    private PrettySpann.TagClickListener mTagClickListener;
    private UserProfileClickListener mUserProfileClickListener;
    private ReshareBodyClickListener mReshareBodyClickListener;

    public ResharePostView(Context context,
                           String date,
                           String shareCount,
                           String likeCount,
                           String author,
                           String authorId,
                           String title,
                           String body,
                           String postId,
                           PrettySpann.TagClickListener tagClickListener,
                           UserProfileClickListener userProfileClickListener,
                           ReshareBodyClickListener reshareBodyClickListener) {
        super(context);

        mDate = date;
        mShareCount = shareCount;
        mLikeCount = likeCount;
        mAuthor = author;
        mAuthorId = authorId;
        mTitle = title;
        mBody = body;
        mPostId = postId;
        mTagClickListener = tagClickListener;
        mUserProfileClickListener = userProfileClickListener;
        mReshareBodyClickListener = reshareBodyClickListener;

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
        rootLayout.setOnClickListener(this);
        tvDate = (PostTextView) findViewById(R.id.reshare_date);
        tvshareCount = (PostTextView) findViewById(R.id.reshare_count);
        tvLikeCount = (PostTextView) findViewById(R.id.reshare_like_count);
        tvAuthor = (PostTextView) findViewById(R.id.reshare_author_name);
        tvAuthor.setOnClickListener(this);
        tvTitle = (PostTextView) findViewById(R.id.reshare_title);
        bodyView = (PostBodyView) findViewById(R.id.reshare_post_body);
        bodyView.setClickListener(this);

        setData(mDate, mShareCount, mLikeCount, mAuthor, mTitle, mBody, mTagClickListener);
    }


    public void setData(String date, String shareCount, String likeCount, String author,
                        String title, String body, PrettySpann.TagClickListener tagClickListener) {
        tvDate.setText(TimeUtil.getInstance().getReadableDate(date));
        tvshareCount.setText(String.format(reshares, shareCount));
        tvLikeCount.setText(String.format(likes, likeCount));
        tvAuthor.setText(author);

        if (title == null || title.isEmpty()) {
            tvTitle.setVisibility(GONE);
        } else {
            tvTitle.setText(title);
        }

        bodyView.setPostBody(new PostBody(body, null, tagClickListener));
    }

    public void changeBackgroundColor(int color) {
        rootLayout.setBackgroundColor(color);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.reshare_author_name) {
            if (mUserProfileClickListener != null) {
                mUserProfileClickListener.showUserProfile(mAuthorId);
            }
        } else {
            // clicked anywhere but title
            if (mReshareBodyClickListener != null) {
                mReshareBodyClickListener.onReshareBodyClicked(mPostId);
            }
        }
    }
}
