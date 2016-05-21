/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.models;

import com.mehdok.singlepostviewlib.interfaces.SendCommentClickListener;

/**
 * Created by mehdok on 5/4/2016.
 */
public class Post {
    private PostDetail postDetail;
    private PostBody postBody;
    private PostFunction postFunction;
    private SendCommentClickListener sendCommentClickListener;
    private String postTitle;
    private String postId;

    public Post(PostDetail postDetail, PostBody postBody,
                PostFunction postFunction,
                SendCommentClickListener sendCommentClickListener, String postTitle,
                String postId) {
        this.postDetail = postDetail;
        this.postBody = postBody;
        this.postFunction = postFunction;
        this.sendCommentClickListener = sendCommentClickListener;
        this.postTitle = postTitle;
        this.postId = postId;
    }

    public PostDetail getPostDetail() {
        return postDetail;
    }

    public PostBody getPostBody() {
        return postBody;
    }

    public PostFunction getPostFunction() {
        return postFunction;
    }

    public SendCommentClickListener getSendCommentClickListener() {
        return sendCommentClickListener;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostId() {
        return postId;
    }
}
