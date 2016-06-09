/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mehdok.gooder.ui.profile.ProfileActivity;
import com.mehdok.gooder.ui.singlepost.SinglePostActivity;

public class LinkCheckerActivity extends AppCompatActivity {

    private final String USER = "user";
    private final String POST = "post";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_checker);

        String address = getIntent().getData().toString();
        if (address.contains(USER)) {
            // this is user profile
            int last = address.lastIndexOf("/");
            String userId = address.substring(last + 1, address.length());
            openUserProfile(userId);
        } else if (address.contains(POST)) {
            // this is post profile
            int last = address.lastIndexOf("/");
            String postId = address.substring(last + 1, address.length());
            openPostSingleView(postId);
        }

        finish();
    }

    private void openUserProfile(String userId) {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra(ProfileActivity.PROFILE_USER_ID, userId);
        startActivity(profileIntent);
    }

    private void openPostSingleView(String postId) {
        Intent intent = new Intent(this, SinglePostActivity.class);
        intent.putExtra(SinglePostActivity.POST_ID_EXTRA, postId);
        startActivity(intent);
    }
}
