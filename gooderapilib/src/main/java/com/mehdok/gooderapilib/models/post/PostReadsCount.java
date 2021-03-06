/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/2/2016.
 */
public class PostReadsCount extends com.mehdok.gooderapilib.models.BaseResponse {
    @SerializedName("msg_data")
    @Expose
    public Integer readCount;

    public Integer getReadCount() {
        return readCount;
    }
}
