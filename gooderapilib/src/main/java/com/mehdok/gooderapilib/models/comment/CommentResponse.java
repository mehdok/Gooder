/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mehdok.gooderapilib.models.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehdok on 5/5/2016.
 */
public class CommentResponse extends BaseResponse {
    @SerializedName("msg_data")
    @Expose
    private List<CommentContent> commentList = new ArrayList<CommentContent>();
}
