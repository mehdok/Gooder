/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.profile.views;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.mehdok.gooder.R;

/**
 * Created by mehdok on 5/18/2016.
 */
public class FollowButton extends AppCompatButton {
    private boolean followed;

    public FollowButton(Context context) {
        super(context);
        init();
    }

    public FollowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FollowButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    public void setFollow(boolean follow) {
        followed = follow;
        if (!follow) {
            setText(R.string.follow);
        } else {
            setText(R.string.unfollow);
        }
    }

    public boolean isFollowed() {
        return followed;
    }
}
