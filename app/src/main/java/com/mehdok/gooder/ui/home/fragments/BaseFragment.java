/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;

import com.mehdok.gooder.Globals;
import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.infinitescroll.interfaces.InfiniteScrollListener;
import com.mehdok.gooder.infinitescroll.interfaces.UiToggleListener;
import com.mehdok.gooder.infinitescroll.views.InfiniteRecyclerView;
import com.mehdok.gooder.ui.home.adapters.SinglePostAdapter;
import com.mehdok.gooder.ui.home.models.ParcelablePost;
import com.mehdok.gooder.ui.home.navigation.MainActivityDelegate;
import com.mehdok.gooder.ui.profile.ProfileActivity;
import com.mehdok.gooder.utils.ReshareUtil;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.models.post.APIPost;
import com.mehdok.gooderapilib.models.post.ReshareChain;
import com.mehdok.gooderapilib.models.user.UserInfo;
import com.mehdok.singlepostviewlib.interfaces.UserProfileClickListener;

import java.util.ArrayList;

/**
 * Created by mehdok on 5/7/2016.
 */
public abstract class BaseFragment extends Fragment implements InfiniteScrollListener,
        UiToggleListener, ReshareUtil.ReshareChainListener,
        UserProfileClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    protected InfiniteRecyclerView mRecyclerView;
    protected SinglePostAdapter mAdapter;
    protected ArrayList<APIPost> mPosts;
    protected SwipeRefreshLayout refreshLayout;
    protected boolean loadingFlag = false;
    protected boolean reachEndOfPosts = false;

    protected abstract String getFragmentTag();

    protected abstract void getData();

    protected void showProgress(final boolean show) {
        loadingFlag = show;

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(show);
            }
        });
    }

    @Override
    public void loadMore() {
        if (!loadingFlag) {
            getData();
        }
    }

    public void clearViews() {
        mPosts.clear();
        mAdapter = null;
    }

    @Override
    public void show() {
        MainActivityDelegate.getInstance().getActivity().showUI();
    }

    @Override
    public void hide() {
        MainActivityDelegate.getInstance().getActivity().hideUI();
    }


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(postContentChangeReceiver, new IntentFilter(
                        Globals.POST_CONTENT_CHANGED));
    }

    @Override
    public void onPause() {
        super.onPause();
        //        LocalBroadcastManager.getInstance(getActivity())
        //                .unregisterReceiver(postContentChangeReceiver);
    }

    private BroadcastReceiver postContentChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ParcelablePost parcelablePost = intent.getParcelableExtra(Globals.CHANGED_POST);
            if (mPosts != null) {
                for (APIPost post : mPosts) {
                    if (post.getPid().equals(parcelablePost.getPid())) {
                        post.setLiked(parcelablePost.isLiked());
                        post.setStarred(parcelablePost.isStared());
                        post.setRead(parcelablePost.isRead());
                        post.setLikeCounts(parcelablePost.getLikeCounts());
                        post.setCommentCount(parcelablePost.getCommentCount());
                        post.setSharesCount(parcelablePost.getSharesCount());

                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }

                        break;
                    }
                }
            }
        }
    };

    protected void checkForReshares(int from, UserInfo userInfo) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(userInfo.getUsername());
        try {
            queryBuilder.setPassword(Crypto.getMD5BASE64(
                    new String(Crypto.decrypt(userInfo.getPassword(), getActivity()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ReshareUtil reshareUtil = new ReshareUtil();
        reshareUtil.setReshareChainListener(this);
        reshareUtil.getReshareChain(mPosts, from, queryBuilder);
    }

    @Override
    public void onReshareChainFetched(ReshareChain reshareChain) {
        mPosts.get(reshareChain.getPosition()).setReshareChains(reshareChain.getPosts());
        mAdapter.notifyItemChanged(reshareChain.getPosition());
    }

    @Override
    public void showUserProfile(String userID) {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.PROFILE_USER_ID, userID);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(), null);
        ActivityCompat.startActivity(getActivity(),
                intent, options.toBundle());
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    public void refreshData() {
        reachEndOfPosts = false;
        mPosts.clear();
        getData();
    }
}
