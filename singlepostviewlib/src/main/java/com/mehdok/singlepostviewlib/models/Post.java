/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.models;

import com.mehdok.singlepostviewlib.interfaces.SendCommentClickListener;

import java.util.ArrayList;

/**
 * Created by mehdok on 5/4/2016.
 */
public class Post {
    private PostDetail postDetail;
    private PostBody postBody;
    private PostFunction postFunction;
    private ArrayList<PostComment> postComment;
    private SendCommentClickListener sendCommentClickListener;

    public Post(PostDetail postDetail, PostBody postBody,
                PostFunction postFunction, ArrayList<PostComment> postComment,
                SendCommentClickListener sendCommentClickListener) {
        this.postDetail = postDetail;
        this.postBody = postBody;
        this.postFunction = postFunction;
        this.postComment = postComment;
        this.sendCommentClickListener = sendCommentClickListener;
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

    public ArrayList<PostComment> getPostComment() {
        return postComment;
    }

    public SendCommentClickListener getSendCommentClickListener() {
        return sendCommentClickListener;
    }
}
