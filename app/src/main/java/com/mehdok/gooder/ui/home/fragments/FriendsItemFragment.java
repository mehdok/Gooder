/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mehdok.gooder.R;
import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.database.DatabaseHelper;
import com.mehdok.gooder.infinitescroll.interfaces.InfiniteScrollListener;
import com.mehdok.gooder.infinitescroll.interfaces.UiToggleListener;
import com.mehdok.gooder.infinitescroll.views.InfiniteRecyclerView;
import com.mehdok.gooder.ui.home.adapters.SinglePostAdapter;
import com.mehdok.gooder.ui.home.navigation.MainActivityDelegate;
import com.mehdok.gooder.views.VerticalSpaceItemDecoration;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.post.Post;
import com.mehdok.gooderapilib.models.post.Posts;
import com.mehdok.gooderapilib.models.user.UserInfo;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsItemFragment extends BaseFragment implements InfiniteScrollListener,
        UiToggleListener {
    private static FriendsItemFragment mInstance;
    private InfiniteRecyclerView mRecyclerView;
    private ProgressBar mProgress;
    private SinglePostAdapter mAdapter;
    private ArrayList<Post> mPosts;

    private boolean loadingFlag = false;

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
            mAdapter = new SinglePostAdapter(getActivity(), mPosts);
            mRecyclerView.setAdapter(mAdapter);

            //TODO set recycler listener

            mProgress = (ProgressBar) v.findViewById(R.id.friends_item_progress);

            getData();
        } else {
            mRecyclerView.setAdapter(mAdapter);
        }

        return v;
    }

    private void showProgress(boolean show) {
        loadingFlag = show;

        if (show) {
            mProgress.setVisibility(View.VISIBLE);
        } else {
            mProgress.setVisibility(View.INVISIBLE);
        }
    }

    private void getData() {
        showProgress(true);

        UserInfo userInfo = DatabaseHelper.getInstance(getActivity()).getUserInfo();

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
        //        queryBuilder.setUnreadOnly(QueryBuilder.Value.YES);
        //        queryBuilder.setReverseOrder(QueryBuilder.Value.NO);
        queryBuilder.setStart(mPosts.size());
        requestBuilder.getAllFriendsItem(queryBuilder)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Posts>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showProgress(false);
                        MainActivityDelegate.getInstance().getActivity().showBugSnackBar(e);
                    }

                    @Override
                    public void onNext(Posts posts) {
                        showProgress(false);
                        if (posts != null) {
                            mPosts.addAll(posts.getPosts());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
}
