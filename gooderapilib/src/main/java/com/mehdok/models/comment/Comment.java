/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mehdok.models.BaseResponse;

/**
 * Created by mehdok on 5/2/2016.
 */
public class Comment extends BaseResponse {
    @SerializedName("msg_data")
    @Expose
    public CommentInfo commentInfo;

    public CommentInfo getCommentInfo() {
        return commentInfo;
    }
}
