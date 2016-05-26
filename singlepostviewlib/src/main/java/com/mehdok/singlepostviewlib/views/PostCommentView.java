/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.interfaces.CommentMoreListener;
import com.mehdok.singlepostviewlib.interfaces.UserProfileClickListener;
import com.mehdok.singlepostviewlib.models.PostDetail;
import com.mehdok.singlepostviewlib.utils.ClipBoardUtil;
import com.mehdok.singlepostviewlib.utils.PrettySpann;
import com.mehdok.singlepostviewlib.utils.httpimage.URLImageParser;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostCommentView extends LinearLayout implements CommentMoreListener {

    private PostTextView tvBody;
    private PostDetailView postDetail;

    private String commentAuthorId;
    private String commentBody;

    public PostCommentView(Context context) {
        super(context);
        init(context);
    }

    public PostCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostCommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.post_comment, this);

        tvBody = (PostTextView) findViewById(R.id.comment_body);
        postDetail = (PostDetailView) findViewById(R.id.post_comment_detail);
    }

    public void setComment(String uid, String author, String date, String body, String authorPhoto,
                           UserProfileClickListener profileClickListener) {
        tvBody.setPrettyText(PrettySpann.getPrettyString(body, null, new URLImageParser(tvBody)));
        postDetail.setPostDetail(new PostDetail(uid, author, date, profileClickListener),
                PostDetailView.More.COMMENT, null, this, null, 0, null);
        postDetail.loadAuthorPhoto(authorPhoto);

        commentAuthorId = uid;
        commentBody = body;
    }

    @Override
    public void copyCommentAuthorId() {
        ClipBoardUtil.copyText(getContext(), commentAuthorId);
        showToast(R.string.clip_comment_author);
    }

    @Override
    public void copyCommentText() {
        ClipBoardUtil.copyText(getContext(), commentBody);
        showToast(R.string.clip_comment_body);
    }

    private void showToast(int resourceId) {
        Toast.makeText(getContext(), resourceId, Toast.LENGTH_SHORT).show();
    }
}
