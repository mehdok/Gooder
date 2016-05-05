/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/5/2016.
 */
public class CommentData {
    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("contents")
    @Expose
    private String content;

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }
}
