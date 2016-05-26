/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mehdok.gooder.R;
import com.mehdok.gooder.ui.singlepost.SinglePostActivity;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.models.notification.Notification;
import com.mehdok.gooderapilib.models.user.UserInfo;
import com.mehdok.singlepostviewlib.interfaces.NotificationMoreListener;
import com.mehdok.singlepostviewlib.interfaces.UserProfileClickListener;
import com.mehdok.singlepostviewlib.models.PostDetail;
import com.mehdok.singlepostviewlib.views.PostDetailView;
import com.mehdok.singlepostviewlib.views.PostTextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by mehdok on 5/25/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ItemViewHolder>
        implements
        NotificationMoreListener {

    private ArrayList<Notification> mNotification;
    private ArrayList<UserInfo> mUserInfo;
    private WeakReference<Context> mContext;
    private UserProfileClickListener userProfileClickListener;

    public NotificationAdapter(Context ctx, ArrayList<Notification> notification,
                               ArrayList<UserInfo> userInfos,
                               UserProfileClickListener userProfileClickListener) {
        mNotification = notification;
        mContext = new WeakReference<Context>(ctx);
        this.userProfileClickListener = userProfileClickListener;
        mUserInfo = userInfos;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Notification notification = mNotification.get(position);
        UserInfo userInfo = getUserInfo(notification.getUid());
        String userName = userInfo == null ? "" : userInfo.getFullname();
        String authorUrl = userInfo == null ? null : userInfo.getAvatar();

        //TODO handle More delete
        PostDetail postDetail =
                new PostDetail(notification.getUid(), userName, notification.getTime(),
                        userProfileClickListener);
        holder.notificationDetail.setPostDetail(postDetail, PostDetailView.More.NOTIFICATION, null,
                null, this, position, authorUrl);
        //TODO msg may be null for some type of notification
        setNotifBody(holder.notificationBody, notification.getType());
    }

    @Override
    public int getItemCount() {
        if (mNotification == null) return 0;

        return mNotification.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public PostDetailView notificationDetail;
        public PostTextView notificationBody;

        public ItemViewHolder(View view) {
            super(view);

            notificationDetail = (PostDetailView) view.findViewById(R.id.notification_user_detail);
            notificationBody = (PostTextView) view.findViewById(R.id.notification_body);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if (mNotification.get(pos).getNotificationData() != null) {
                Intent singlePostIntent = new Intent(view.getContext(), SinglePostActivity.class);
                singlePostIntent.putExtra(SinglePostActivity.POST_ID_EXTRA,
                        mNotification.get(pos).getNotificationData().getPid());
                view.getContext().startActivity(singlePostIntent);
            }
        }
    }

    @Override
    public void deleteNotification(int pos) {

    }

    private String getUserName(String uid) {
        for (UserInfo userInfo : mUserInfo) {
            if (userInfo.getUid().equals(uid)) {
                return userInfo.getFullname();
            }
        }

        return "";
    }

    private UserInfo getUserInfo(String uid) {
        for (UserInfo userInfo : mUserInfo) {
            if (userInfo.getUid().equals(uid)) {
                return userInfo;
            }
        }

        return null;
    }

    private void setNotifBody(TextView body, String notifType) {
        if (notifType.equals(QueryBuilder.NotificationType.COMMENT.toString())) {
            body.setText(R.string.notification_comment);
        } else if (notifType.equals(QueryBuilder.NotificationType.POST.toString())) {
            body.setText(R.string.notification_post);
        } else if (notifType.equals(QueryBuilder.NotificationType.FOLLOWER.toString())) {
            body.setText(R.string.notification_new_follower);
        } else {
            body.setText(notifType);
        }
    }
}
