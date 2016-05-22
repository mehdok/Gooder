/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehdok.gooder.Globals;
import com.mehdok.gooder.R;
import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.database.DatabaseHelper;
import com.mehdok.gooder.infinitescroll.interfaces.InfiniteScrollListener;
import com.mehdok.gooder.infinitescroll.interfaces.UiToggleListener;
import com.mehdok.gooder.infinitescroll.views.InfiniteRecyclerView;
import com.mehdok.gooder.ui.home.adapters.SinglePostAdapter;
import com.mehdok.gooder.ui.home.models.ParcelablePost;
import com.mehdok.gooder.ui.home.navigation.MainActivityDelegate;
import com.mehdok.gooder.ui.profile.ProfileActivity;
import com.mehdok.gooder.utils.ReshareUtil;
import com.mehdok.gooder.views.VerticalSpaceItemDecoration;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.post.APIPost;
import com.mehdok.gooderapilib.models.post.APIPosts;
import com.mehdok.gooderapilib.models.user.UserInfo;
import com.mehdok.singlepostviewlib.interfaces.UserProfileClickListener;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsItemFragment extends BaseFragment implements InfiniteScrollListener,
        UiToggleListener, ReshareUtil.ReshareUpdateListener, UserProfileClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    private String TAG = this.getClass().getSimpleName();
    private static FriendsItemFragment mInstance;
    private InfiniteRecyclerView mRecyclerView;
    private SinglePostAdapter mAdapter;
    private ArrayList<APIPost> mPosts;
    private SwipeRefreshLayout refreshLayout;

    private boolean loadingFlag = false;
    private boolean reachEndOfPosts = false;

    public static FriendsItemFragment getInstance() {
        if (mInstance == null) {
            mInstance = new FriendsItemFragment();
        }

        return mInstance;
    }

    public FriendsItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_friends_item, container, false);

        // setup refresh action
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.friends_refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.refresh_color_1,
                R.color.refresh_color_2,
                R.color.refresh_color_3,
                R.color.refresh_color_4);
        refreshLayout.setOnRefreshListener(this);

        // init recycler
        mRecyclerView = (InfiniteRecyclerView) v.findViewById(R.id.friends_item_recycler);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(
                getActivity().getResources().getDimensionPixelSize(R.dimen.standard_padding)));
        mRecyclerView.setInfiniteScrollListener(this);
        mRecyclerView.setUiToggleListener(this);

        if (mAdapter == null) {
            mPosts = new ArrayList<>();
            mAdapter = new SinglePostAdapter(getActivity(), mPosts, this);
            mRecyclerView.setAdapter(mAdapter);

            getData();
        } else {
            mRecyclerView.setAdapter(mAdapter);
        }

        return v;
    }

    private void showProgress(final boolean show) {
        loadingFlag = show;

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(show);
            }
        });
    }

    private void getData() {
        // return if there is no more posts
        if (reachEndOfPosts) return;

        showProgress(true);

        final UserInfo userInfo = DatabaseHelper.getInstance(getActivity()).getUserInfo();

        RequestBuilder requestBuilder = new RequestBuilder();
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(userInfo.getUsername());
        try {
            queryBuilder.setPassword(Crypto.getMD5BASE64(
                    new String(Crypto.decrypt(userInfo.getPassword(), getActivity()))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //        queryBuilder.setGid("");
        queryBuilder.setUnreadOnly(MainActivityDelegate.getInstance().getActivity().isUnreadOnly());
        queryBuilder.setReverseOrder(
                MainActivityDelegate.getInstance().getActivity().isReverseOrder());
        queryBuilder.setStart(mPosts.size());
        requestBuilder.getAllFriendsItem(queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<APIPosts>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showProgress(false);
                        MainActivityDelegate.getInstance().getActivity().showBugSnackBar(e);
                    }

                    @Override
                    public void onNext(APIPosts posts) {
                        showProgress(false);
                        if (posts != null) {

                            // if there is no more posts, show message and return
                            if (posts.getPosts().size() == 0) {
                                reachEndOfPosts = true;
                                MainActivityDelegate.getInstance()
                                        .getActivity()
                                        .showSimpleMessage(getString(R.string.last_post));
                                return;
                            }

                            mPosts.addAll(posts.getPosts());
                            mAdapter.notifyDataSetChanged();

                            checkForReshares(mPosts.size() - posts.getPosts().size(), userInfo);
                        }
                    }
                });
    }

    @Override
    public void loadMore() {
        if (!loadingFlag) {
            getData();
        }
    }

    @Override
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

    private void checkForReshares(int from, UserInfo userInfo) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(userInfo.getUsername());
        try {
            queryBuilder.setPassword(Crypto.getMD5BASE64(
                    new String(Crypto.decrypt(userInfo.getPassword(), getActivity()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ReshareUtil reshareUtil = new ReshareUtil();
        reshareUtil.setListener(FriendsItemFragment.this);
        reshareUtil.checkForReshares(mPosts, from, queryBuilder);
    }

    @Override
    public void ResharePostFetched(int position) {
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void showUserProfile(String userID) {
        Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
        profileIntent.putExtra(ProfileActivity.PROFILE_USER_ID, userID);
        getActivity().startActivity(profileIntent);
    }

    @Override
    public void onRefresh() {
        refreshData();
    }


    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public void refreshData() {
        reachEndOfPosts = false;
        mPosts.clear();
        getData();
    }
}
