/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.models.PostDetail;
import com.mehdok.singlepostviewlib.utils.GlideHelper;
import com.mehdok.singlepostviewlib.utils.TimeUtil;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostDetailView extends LinearLayout implements View.OnClickListener {
    private PostTextView tvAuthor;
    private PostTextView tvDate;
    private ImageView imvAuthor;
    private PostDetail mPostDetail;

    public PostDetailView(Context context) {
        super(context);
        init(context);
    }

    public PostDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.post_detail, this);

        setOnClickListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvAuthor = (PostTextView) findViewById(R.id.author_name);
        tvDate = (PostTextView) findViewById(R.id.post_date);
        imvAuthor = (ImageView) findViewById(R.id.author_pic);
    }

    public void setPostDetail(PostDetail postDetail) {
        mPostDetail = postDetail;
        tvAuthor.setText(postDetail.getAuthor());
        tvDate.setText(TimeUtil.getInstance().getReadableDate(postDetail.getDate()));
    }

    public void loadAuthorPhoto(String url) {
        imvAuthor.setVisibility(VISIBLE);
        GlideHelper.loadProfileImage(getContext(), url, imvAuthor, null);
    }

    @Override
    public void onClick(View view) {
        if (mPostDetail.getProfileClickListener() != null) {
            mPostDetail.getProfileClickListener().showUserProfile(mPostDetail.getUid());
        }
    }

    public void hideUserPhoto() {
        imvAuthor.setVisibility(GONE);
    }
}
