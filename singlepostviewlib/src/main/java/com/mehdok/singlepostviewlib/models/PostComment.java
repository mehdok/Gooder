/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.models;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostComment {
    private String author;
    private String date;
    private String body;
    private String authorUrl;

    public PostComment(String author, String date, String body, String authorUrl) {
        this.author = author;
        this.date = date;
        this.body = body;
        this.authorUrl = authorUrl;
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
}
