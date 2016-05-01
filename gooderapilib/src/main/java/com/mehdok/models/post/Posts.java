/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mehdok on 5/1/2016.
 */
public class Posts {
    @SerializedName("msg_code") @Expose private String msgCode;
    @SerializedName("msg_type") @Expose private String msgType;
    @SerializedName("msg_text") @Expose private String msgText;
    @SerializedName("msg_data") @Expose private List<Post> posts;

    public String getMsgCode() {
        return msgCode;
    }

    public String getMsgType() {
        return msgType;
    }

    public String getMsgText() {
        return msgText;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
