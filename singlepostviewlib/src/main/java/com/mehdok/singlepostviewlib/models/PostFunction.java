/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.models;

import com.mehdok.singlepostviewlib.interfaces.FunctionButtonClickListener;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostFunction {
    private int likeCount;
    private int shareCount;
    private int commentCount;
    private FunctionButtonClickListener listener;

    public PostFunction(int likeCount, int shareCount, int commentCount,
                        FunctionButtonClickListener listener) {
        this.likeCount = likeCount;
        this.shareCount = shareCount;
        this.commentCount = commentCount;
        this.listener = listener;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public FunctionButtonClickListener getListener() {
        return listener;
    }
}
