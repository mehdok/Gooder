/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.models;

import com.mehdok.singlepostviewlib.interfaces.UserProfileClickListener;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostDetail {
    private String uid;
    private String author;
    private String date;
    private UserProfileClickListener profileClickListener;

    public PostDetail(String uid, String author, String date,
                      UserProfileClickListener profileClickListener) {
        this.uid = uid;
        this.author = author;
        this.date = date;
        this.profileClickListener = profileClickListener;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getUid() {
        return uid;
    }

    public UserProfileClickListener getProfileClickListener() {
        return profileClickListener;
    }
}
