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
import com.mehdok.gooder.infinitescroll.views.InfiniteRecyclerView;
import com.mehdok.gooder.ui.home.adapters.SinglePostAdapter;
import com.mehdok.gooder.ui.home.models.PrettyPost;
import com.mehdok.gooder.ui.home.navigation.MainActivityDelegate;
import com.mehdok.gooder.views.VerticalSpaceItemDecoration;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.post.Post;
import com.mehdok.gooderapilib.models.post.Posts;
import com.mehdok.gooderapilib.models.user.UserInfo;

import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsItemFragment extends Fragment implements InfiniteScrollListener {
    private static FriendsItemFragment mInstance;
    private InfiniteRecyclerView mRecyclerView;
    private ProgressBar mProgress;
    private SinglePostAdapter mAdapter;
    private ArrayList<PrettyPost> mPosts;

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
        mRecyclerView.setListener(this);

        if (mAdapter == null) {
            mPosts = new ArrayList<>();
            mAdapter = new SinglePostAdapter(mPosts);
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
                // map returned list to pretty span
                .flatMap(new Func1<Posts, Observable<ArrayList<PrettyPost>>>() {
                    @Override
                    public Observable<ArrayList<PrettyPost>> call(Posts posts) {
                        ArrayList<PrettyPost> prettyPosts =
                                new ArrayList<PrettyPost>(posts.getPosts().size());
                        for (Post post : posts.getPosts())
                            prettyPosts.add(new PrettyPost(post));
                        return Observable.just(prettyPosts);
                    }
                })
                .subscribe(new Observer<ArrayList<PrettyPost>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showProgress(false);
                        String error;
                        if (e instanceof HttpException) {
                            error = ((HttpException) e).response().body().toString();
                        } else {
                            error = e.getMessage();
                        }
                        MainActivityDelegate.getInstance().getActivity().showBugSnackBar(error);
                    }

                    @Override
                    public void onNext(ArrayList<PrettyPost> posts) {
                        showProgress(false);
                        if (posts != null) {
                            mPosts.addAll(posts);
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
}
