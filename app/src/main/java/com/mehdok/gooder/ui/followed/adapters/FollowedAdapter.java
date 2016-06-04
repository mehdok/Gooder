/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.followed.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehdok.gooder.R;
import com.mehdok.gooder.ui.profile.ProfileActivity;
import com.mehdok.gooderapilib.models.follow.FollowedInfo;
import com.mehdok.gooderapilib.models.user.UserInfo;
import com.mehdok.singlepostviewlib.interfaces.UserProfileClickListener;
import com.mehdok.singlepostviewlib.models.PostDetail;
import com.mehdok.singlepostviewlib.views.PostDetailView;

import java.util.ArrayList;

/**
 * Created by mehdok on 6/3/2016.
 */
public class FollowedAdapter extends RecyclerView.Adapter<FollowedAdapter.ItemViewHolder> {

    private ArrayList<FollowedInfo> followedInfos;
    private ArrayList<UserInfo> mUserInfo;
    private UserProfileClickListener userProfileClickListener;
    private String unreadCount;

    public FollowedAdapter(Context ctx,
                           ArrayList<FollowedInfo> followedInfos,
                           ArrayList<UserInfo> userInfo,
                           UserProfileClickListener userProfileClickListener) {
        this.followedInfos = followedInfos;
        this.mUserInfo = userInfo;
        this.userProfileClickListener = userProfileClickListener;
        unreadCount = ctx.getString(R.string.unread_count);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_followed, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        FollowedInfo followedInfo = followedInfos.get(position);
        UserInfo userInfo = getUserInfo(String.valueOf(followedInfo.getUid()));
        String userName = userInfo == null ? "" : userInfo.getFullname();
        String authorUrl = userInfo == null ? null : userInfo.getAvatar();
        PostDetail postDetail =
                new PostDetail(String.valueOf(followedInfo.getUid()), userName, "",
                        userProfileClickListener);
        holder.followedDetail.setPostDetail(postDetail, PostDetailView.More.NOTHING, null,
                null, null, position, authorUrl);
        String count = "<b>" + followedInfos.get(position).getUnreads() + "</b> ";
        holder.unreadCount.setText(Html.fromHtml(
                String.format(unreadCount, count)));
    }

    @Override
    public int getItemCount() {
        return followedInfos.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public PostDetailView followedDetail;
        public AppCompatTextView unreadCount;

        public ItemViewHolder(View view) {
            super(view);

            followedDetail = (PostDetailView) view.findViewById(R.id.followed_user_detail);
            unreadCount = (AppCompatTextView) view.findViewById(R.id.followed_unread_count);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Intent intent = new Intent(view.getContext(), ProfileActivity.class);
            intent.putExtra(ProfileActivity.PROFILE_USER_ID,
                    String.valueOf(followedInfos.get(pos).getUid()));
            view.getContext().startActivity(intent);
        }
    }

    private UserInfo getUserInfo(String uid) {
        for (UserInfo userInfo : mUserInfo) {
            if (userInfo.getUid().equals(uid)) {
                return userInfo;
            }
        }

        return null;
    }
}

