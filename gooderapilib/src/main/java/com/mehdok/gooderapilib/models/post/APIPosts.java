/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mehdok on 5/1/2016.
 */
public class APIPosts extends com.mehdok.gooderapilib.models.BaseResponse {
    @SerializedName("msg_data")
    @Expose
    private List<APIPost> posts;

    public List<APIPost> getPosts() {
        return posts;
    }
}
