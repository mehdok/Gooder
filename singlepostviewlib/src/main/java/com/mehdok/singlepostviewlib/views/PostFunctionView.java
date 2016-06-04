/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.interfaces.FunctionButtonClickListener;
import com.mehdok.singlepostviewlib.models.PostFunction;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostFunctionView extends RelativeLayout implements View.OnClickListener {

    private ImageButton btnLike;
    private ImageButton btnStar;
    private ImageButton btnShare;
    private ImageButton btnRead;
    private TextView tvLikeCount;
    private TextView tvShareCount;
    private TextView tvCommentCount;
    private FunctionButtonClickListener listener;

    public PostFunctionView(Context context) {
        super(context);
        init(context);
    }

    public PostFunctionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostFunctionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.post_function, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnLike = (ImageButton) findViewById(R.id.like_button);
        btnShare = (ImageButton) findViewById(R.id.share_button);
        btnStar = (ImageButton) findViewById(R.id.star_button);
        btnRead = (ImageButton) findViewById(R.id.read_button);
        btnLike.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnStar.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        tvLikeCount = (TextView) findViewById(R.id.like_count);
        tvCommentCount = (TextView) findViewById(R.id.comment_count);
        tvShareCount = (TextView) findViewById(R.id.share_count);
    }

    public void setPostFunction(PostFunction postFunction) {
        tvLikeCount.setText(getCount(postFunction.getLikeCount()));
        tvShareCount.setText(getCount(postFunction.getShareCount()));
        tvCommentCount.setText(getCount(postFunction.getCommentCount()));
        this.listener = postFunction.getListener();
    }

    private String getCount(int count) {
        if (count == 0) {
            return "";
        } else {
            return String.valueOf(count);
        }
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            int id = view.getId();
            if (id == R.id.like_button) {
                listener.likeClicked();
            } else if (id == R.id.share_button) {
                listener.shareClicked();
            } else if (id == R.id.star_button) {
                listener.starClicked();
            } else if (id == R.id.read_button) {
                listener.readClicked();
            }
        }
    }

    public void changeLikeIcon(int resourceId) {
        btnLike.setImageResource(resourceId);
    }

    public void changeStarIcon(int resourceId) {
        btnStar.setImageResource(resourceId);
    }

    public void changeLikeCount(int count) {
        tvLikeCount.setText(getCount(count));
    }

    public void changeShareCount(int count) {
        tvShareCount.setText(getCount(count));
    }

    public void changeCommentCount(int count) {
        tvCommentCount.setText(getCount(count));
    }

    public void changeReadIcon(boolean read) {
        if (read) {
            btnRead.setImageResource(R.drawable.tick_fill);
        } else {
            btnRead.setImageResource(R.drawable.tick_empty);
        }
    }
}
