/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.interfaces.CommentMoreListener;
import com.mehdok.singlepostviewlib.interfaces.NotificationMoreListener;
import com.mehdok.singlepostviewlib.interfaces.PostMoreListener;
import com.mehdok.singlepostviewlib.models.PostDetail;
import com.mehdok.singlepostviewlib.utils.GlideHelper;
import com.mehdok.singlepostviewlib.utils.TimeUtil;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostDetailView extends LinearLayout implements View.OnClickListener {
    public enum More {POST, COMMENT, NOTIFICATION}

    private PostTextView tvAuthor;
    private PostTextView tvDate;
    private ImageView imvAuthor;
    private PostDetail mPostDetail;
    private AppCompatImageButton imgMore;
    private More mDetailMode;
    private Dialog postMoreDialog;
    private Dialog commentMoreDialog;
    private Dialog notificationMoreDialog;
    private PostMoreListener postMoreListener;
    private CommentMoreListener commentMoreListener;
    private NotificationMoreListener notificationMoreListener;
    private int mPosition;

    public PostDetailView(Context context) {
        super(context);
        init(context);
    }

    public PostDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.post_detail, this);

        //setOnClickListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvAuthor = (PostTextView) findViewById(R.id.author_name);
        tvDate = (PostTextView) findViewById(R.id.post_date);
        imvAuthor = (ImageView) findViewById(R.id.author_pic);
        imgMore = (AppCompatImageButton) findViewById(R.id.post_more_button);
        imgMore.setOnClickListener(this);
        tvAuthor.setOnClickListener(this);
        imvAuthor.setOnClickListener(this);
    }

    public void setPostDetail(PostDetail postDetail, More detailMode,
                              PostMoreListener postMoreListener,
                              CommentMoreListener commentMoreListener,
                              NotificationMoreListener notificationMoreListener,
                              int position, String authorPhoto) {
        mPostDetail = postDetail;
        tvAuthor.setText(postDetail.getAuthor());
        tvDate.setText(TimeUtil.getInstance().getReadableDate(postDetail.getDate()));
        mDetailMode = detailMode;
        this.postMoreListener = postMoreListener;
        this.commentMoreListener = commentMoreListener;
        this.notificationMoreListener = notificationMoreListener;
        mPosition = position;

        if (authorPhoto != null) {
            loadAuthorPhoto(authorPhoto);
        }
    }

    public void loadAuthorPhoto(String url) {
        imvAuthor.setVisibility(VISIBLE);
        GlideHelper.loadProfileImage(getContext(), url, imvAuthor, null);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.post_more_button) {
            if (mDetailMode == More.POST) {
                showPostMoreOption(view);
            } else if (mDetailMode == More.COMMENT) {
                showCommentMoreOption(view);
            } else if (mDetailMode == More.NOTIFICATION) {
                showNotificationMoreOption(view);
            }
        } else if (id == R.id.more_copy_post_id) {
            hideMoreDialog(More.POST);
            if (postMoreListener != null) {
                postMoreListener.copyPostId(mPosition);
            }
        } else if (id == R.id.more_copy_user_id) {
            hideMoreDialog(More.POST);
            if (postMoreListener != null) {
                postMoreListener.copyPostAuthorId(mPosition);
            }
        } else if (id == R.id.more_copy_text) {
            hideMoreDialog(More.POST);
            if (postMoreListener != null) {
                postMoreListener.copyPostText(mPosition);
            }
        } else if (id == R.id.more_copy_comment_text) {
            hideMoreDialog(More.COMMENT);
            if (commentMoreListener != null) {
                commentMoreListener.copyCommentText();
            }
        } else if (id == R.id.more_copy_comment_author_id) {
            hideMoreDialog(More.COMMENT);
            if (commentMoreListener != null) {
                commentMoreListener.copyCommentAuthorId();
            }
        } else if (id == R.id.more_delete_notification) {
            hideMoreDialog(More.NOTIFICATION);
            if (notificationMoreListener != null) {
                notificationMoreListener.deleteNotification(mPosition);
            }
        } else {
            if (mPostDetail.getProfileClickListener() != null) {
                mPostDetail.getProfileClickListener().showUserProfile(mPostDetail.getUid());
            }
        }
    }

    public void hideUserPhoto() {
        imvAuthor.setVisibility(GONE);
    }

    private void showPostMoreOption(View view) {
        postMoreDialog = new Dialog(view.getContext(), android.R.style.Theme_Holo_Light_Dialog);

        postMoreDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        postMoreDialog.setContentView(R.layout.dialog_post_more);
        WindowManager.LayoutParams wmlp = postMoreDialog.getWindow().getAttributes();
        int[] location = new int[2];
        view.getLocationInWindow(location);
        wmlp.gravity = Gravity.TOP | Gravity.START;
        wmlp.x = location[0] - view.getWidth();
        wmlp.y = location[1];
        wmlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        postMoreDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        PostTextView copyPostId =
                (PostTextView) postMoreDialog.findViewById(R.id.more_copy_post_id);
        PostTextView copyUserId =
                (PostTextView) postMoreDialog.findViewById(R.id.more_copy_user_id);
        PostTextView copyText = (PostTextView) postMoreDialog.findViewById(R.id.more_copy_text);

        copyPostId.setOnClickListener(this);
        copyUserId.setOnClickListener(this);
        copyText.setOnClickListener(this);

        postMoreDialog.show();
    }

    private void showCommentMoreOption(View view) {
        commentMoreDialog = new Dialog(view.getContext(), android.R.style.Theme_Holo_Light_Dialog);

        commentMoreDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        commentMoreDialog.setContentView(R.layout.dialog_comment_more);
        WindowManager.LayoutParams wmlp = commentMoreDialog.getWindow().getAttributes();
        int[] location = new int[2];
        view.getLocationInWindow(location);
        wmlp.gravity = Gravity.TOP | Gravity.START;
        wmlp.x = location[0] - view.getWidth();
        wmlp.y = location[1];
        wmlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        commentMoreDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        PostTextView copyCommentText =
                (PostTextView) commentMoreDialog.findViewById(R.id.more_copy_comment_text);
        PostTextView copyUserId =
                (PostTextView) commentMoreDialog.findViewById(R.id.more_copy_comment_author_id);

        copyCommentText.setOnClickListener(this);
        copyUserId.setOnClickListener(this);

        commentMoreDialog.show();
    }

    private void hideMoreDialog(More more) {
        if (more == More.POST) {
            postMoreDialog.dismiss();
        } else if (more == More.COMMENT) {
            commentMoreDialog.dismiss();
        } else if (more == More.NOTIFICATION) {
            notificationMoreDialog.dismiss();
        }
    }

    private void showNotificationMoreOption(View view) {
        notificationMoreDialog =
                new Dialog(view.getContext(), android.R.style.Theme_Holo_Light_Dialog);

        notificationMoreDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        notificationMoreDialog.setContentView(R.layout.dialog_notification_more);
        WindowManager.LayoutParams wmlp = notificationMoreDialog.getWindow().getAttributes();
        int[] location = new int[2];
        view.getLocationInWindow(location);
        wmlp.gravity = Gravity.TOP | Gravity.START;
        wmlp.x = location[0] - view.getWidth();
        wmlp.y = location[1];
        wmlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        notificationMoreDialog.getWindow()
                .setBackgroundDrawableResource(android.R.color.transparent);


        PostTextView deleteNotification =
                (PostTextView) notificationMoreDialog.findViewById(R.id.more_delete_notification);

        deleteNotification.setOnClickListener(this);

        notificationMoreDialog.show();
    }
}
