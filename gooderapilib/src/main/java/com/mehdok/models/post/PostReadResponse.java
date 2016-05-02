/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mehdok.models.BaseResponse;

/**
 * Created by mehdok on 5/2/2016.
 */
public class PostReadResponse extends BaseResponse {
    @SerializedName("msg_data")
    @Expose
    private String pid;

    public String getPid() {
        return pid;
    }
}
