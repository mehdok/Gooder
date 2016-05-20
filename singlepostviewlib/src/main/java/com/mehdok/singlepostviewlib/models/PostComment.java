/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.models;

import com.mehdok.singlepostviewlib.interfaces.UserProfileClickListener;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostComment {
    private String uid;
    private String author;
    private String date;
    private String body;
    private String authorUrl;
    private UserProfileClickListener profileClickListener;

    public PostComment(String uid, String author, String date, String body, String authorUrl,
                       UserProfileClickListener profileClickListener) {
        this.uid = uid;
        this.author = author;
        this.date = date;
        this.body = body;
        this.authorUrl = authorUrl;
        this.profileClickListener = profileClickListener;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public String getUid() {
        return uid;
    }

    public UserProfileClickListener getProfileClickListener() {
        return profileClickListener;
    }
}
