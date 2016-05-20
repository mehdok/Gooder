/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mehdok.singlepostviewlib.interfaces.UserProfileClickListener;
import com.mehdok.singlepostviewlib.models.PostComment;

import java.util.ArrayList;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostCommentsView extends LinearLayout {
    public PostCommentsView(Context context) {
        super(context);
        init();
    }

    public PostCommentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PostCommentsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
    }

    public void addComment(String uid, String author, String date, String body, String imgUrl,
                           UserProfileClickListener profileClickListener) {
        PostCommentView commentView = new PostCommentView(getContext());
        commentView.setComment(uid, author, date, body, imgUrl, profileClickListener);
        addView(commentView);

    }

    public void addComments(ArrayList<PostComment> comments) {
        for (PostComment comment : comments) {
            addComment(comment.getUid(), comment.getAuthor(), comment.getDate(), comment.getBody(),
                    comment.getAuthorUrl(), comment.getProfileClickListener());
        }
    }
}
