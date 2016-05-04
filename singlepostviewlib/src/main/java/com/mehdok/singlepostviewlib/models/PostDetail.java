/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.models;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostDetail {
    private String author;
    private String date;
    private String authorPhotoUrl;

    public PostDetail(String author, String date, String authorPhotoUrl) {
        this.author = author;
        this.date = date;
        this.authorPhotoUrl = authorPhotoUrl;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getAuthorPhotoUrl() {
        return authorPhotoUrl;
    }
}
