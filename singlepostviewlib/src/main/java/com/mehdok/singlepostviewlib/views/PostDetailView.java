/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.models.PostDetail;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostDetailView extends RelativeLayout {
    private PostTextView tvAuthor;
    private PostTextView tvDate;
    private ImageView imvAuthor;

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
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvAuthor = (PostTextView) findViewById(R.id.author_name);
        tvDate = (PostTextView) findViewById(R.id.post_date);
        imvAuthor = (ImageView) findViewById(R.id.author_pic);
    }

    public void setPostDetail(PostDetail postDetail) {
        tvAuthor.setText(postDetail.getAuthor());
        tvDate.setText(postDetail.getDate());

        Glide
                .with(getContext())
                .load(postDetail.getAuthorPhotoUrl())
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(imvAuthor) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imvAuthor.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
}
