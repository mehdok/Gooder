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

    public PostDetail(String author, String date) {
        this.author = author;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }
}
