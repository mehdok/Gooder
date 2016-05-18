/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.profile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mehdok.gooder.R;
import com.mehdok.gooder.database.DatabaseHelper;
import com.mehdok.gooder.ui.profile.views.FollowButton;
import com.mehdok.gooderapilib.models.user.UserInfo;

public class ProfileActivity extends AppCompatActivity {

    public static final String PROFILE_USER_ID = "profile_user_id";
    private UserInfo mUserInfo;
    private String currentUserId;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FollowButton followButton;

    private AppCompatImageView imgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // setup toolbar
        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get user info
        mUserInfo = DatabaseHelper.getInstance(this).getUserInfo();

        // get extra
        currentUserId = getIntent().getStringExtra(PROFILE_USER_ID);

        // find views
        imgUser = (AppCompatImageView) findViewById(R.id.profile_photo);
        followButton = (FollowButton) findViewById(R.id.profile_follow_button);
        collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.profile_toolbar_layout);

        showProfile();
    }

    private void showProfile() {
        if (isOwnerProfile()) {
            ownerUI();
        } else {
            otherUI();
        }
    }

    private boolean isOwnerProfile() {
        if (currentUserId == null || currentUserId.isEmpty()) {
            currentUserId = mUserInfo.getUid();
            return true;
        }

        return mUserInfo.getUid().equals(currentUserId);
    }

    private void ownerUI() {
        collapsingToolbarLayout.setTitle(mUserInfo.getFullname());
        followButton.setVisibility(View.GONE);
        loadUserImage(mUserInfo.getAvatar());
        //TODO get user post
    }

    private void otherUI() {
        collapsingToolbarLayout.setTitle("");
        //TODO get UserInfo
        // TODO get follow info
        //TODO get User post
    }

    private void loadUserImage(String avatar) {
        Glide
                .with(this)
                .load(avatar)
                .asBitmap()
                .centerCrop()
                .placeholder(R.mipmap.logo)
                .into(new BitmapImageViewTarget(imgUser) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imgUser.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
}
