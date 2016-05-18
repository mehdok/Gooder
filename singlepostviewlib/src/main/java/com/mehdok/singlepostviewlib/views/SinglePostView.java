/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.models.Post;
import com.mehdok.singlepostviewlib.models.PostComment;

import java.util.ArrayList;

/**
 * Created by mehdok on 5/4/2016.
 */
public class SinglePostView extends RelativeLayout {
    private PostDetailView postDetailView;
    private PostBodyView postBodyView;
    private PostFunctionView postFunctionView;
    private PostCommentsView postCommentsView;
    private AddCommentView addCommentView;
    private PostTextView postTitleTextView;

    public SinglePostView(Context context) {
        super(context);
        init(context);
    }

    public SinglePostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SinglePostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.single_post, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        postDetailView = (PostDetailView) findViewById(R.id.post_detail);
        postBodyView = (PostBodyView) findViewById(R.id.post_body);
        postFunctionView = (PostFunctionView) findViewById(R.id.post_function);
        postCommentsView = (PostCommentsView) findViewById(R.id.post_comments);
        addCommentView = (AddCommentView) findViewById(R.id.add_comment);
        postTitleTextView = (PostTextView) findViewById(R.id.single_post_title);
    }

    public void showPost(Post post) {
        postDetailView.setPostDetail(post.getPostDetail());
        postBodyView.setPostBody(post.getPostBody());
        postFunctionView.setPostFunction(post.getPostFunction());

        if (post.getPostTitle().isEmpty()) {
            postTitleTextView.setVisibility(GONE);
        } else {
            postTitleTextView.setText(post.getPostTitle());
        }

        addCommentView.setSendCommentListener(post.getSendCommentClickListener());
    }

    public void addComments(ArrayList<PostComment> postComment) {
        postCommentsView.addComments(postComment);
    }

    public void addComment(PostComment postComment) {
        postCommentsView.addComment(postComment.getAuthor(), postComment.getDate(),
                postComment.getBody(),
                postComment.getAuthorUrl());
    }

    public void addUserPhoto(String authorPhoto) {
        postDetailView.loadAuthorPhoto(authorPhoto);
    }

    public void changeLikeIcon(int resourceId) {
        postFunctionView.changeLikeIcon(resourceId);
    }

    public void changeStarIcon(int resourceId) {
        postFunctionView.changeStarIcon(resourceId);
    }

    public void changeLikeCount(int count) {
        postFunctionView.changeLikeCount(count);
    }

    public void changeShareCount(int count) {
        postFunctionView.changeShareCount(count);
    }

    public void changeCommentCount(int count) {
        postFunctionView.changeCommentCount(count);
    }
}
