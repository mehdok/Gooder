/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mehdok.models.BaseResponse;

import java.util.List;

/**
 * Created by mehdok on 5/1/2016.
 */
public class Posts extends BaseResponse {
    @SerializedName("msg_data")
    @Expose
    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }
}
