/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mehdok.gooderapilib.models.user.UserInfo;

/**
 * Created by mehdok on 5/5/2016.
 */
public class CommentContent {
    @SerializedName("author")
    @Expose
    private UserInfo commentAuthor;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("contents")
    @Expose
    private String content;

    public UserInfo getCommentAuthor() {
        return commentAuthor;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }
}
