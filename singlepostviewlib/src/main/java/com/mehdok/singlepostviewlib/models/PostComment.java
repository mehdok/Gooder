/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.models;

import android.text.SpannableString;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostComment {
    private String author;
    private String date;
    private SpannableString body;

    public PostComment(String author, String date, SpannableString body) {
        this.author = author;
        this.date = date;
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public SpannableString getBody() {
        return body;
    }
}
