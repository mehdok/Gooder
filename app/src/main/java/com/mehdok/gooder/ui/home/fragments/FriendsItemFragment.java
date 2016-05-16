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
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import com.mehdok.gooder.views.VerticalSpaceItemDecoration;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.post.APIPost;
import com.mehdok.gooderapilib.models.post.APIPosts;
import com.mehdok.gooderapilib.models.post.SinglePost;
import com.mehdok.gooderapilib.models.user.UserInfo;
import com.mehdok.singlepostviewlib.utils.PrettySpann;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
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
    private ArrayList<APIPost> mPosts;

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
                            mPosts.addAll(posts.getPosts());
                            mAdapter.notifyDataSetChanged();

                            //TODO test
                            checkForReshares(mPosts.size() - posts.getPosts().size());
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

    private void checkForReshares(int from) {
        for (int i = from; i < mPosts.size(); i++) {
            if (!mPosts.get(i).getParentPid().equals("0")) {
                getResharedPost(i, mPosts.get(i).getAuthor().getFullName(),
                        mPosts.get(i).getPostBody(), mPosts.get(i).getParentPid(), 0);
            }
        }
    }

    private void checkForReshares(List<APIPost> posts) {
        Observable.from(posts)
                .subscribe(new Observer<APIPost>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(APIPost apiPost) {
                        if (apiPost.getParentPid().equals("0")) {

                        }
                    }
                });
    }

    /**
     * @param pos
     * @param userName
     * @param postBody
     * @param parentId
     * @param count    if count > 1 then include body an name of second resharer
     */
    private void getResharedPost(final int pos, final String userName, final String postBody,
                                 String parentId, int count) {
        ++count;
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

        final int finalCount = count;
        requestBuilder.getPost(parentId, queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SinglePost>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(SinglePost singlePost) {
                        if (!singlePost.getPost().getParentPid().equals("0")) {
                            getResharedPost(pos, singlePost.getPost().getAuthor().getFullName(),
                                    singlePost.getPost().getPostBody(),
                                    singlePost.getPost().getParentPid(), finalCount + 1);
                        } else {
                            APIPost apiPost = mPosts.get(pos);
                            String editedPost = "";
                            if (finalCount > 1) {
                                // more than 1 reshare

                            }

                            editedPost += String.format("%s%s%s%s%s",
                                    PrettySpann.SHARE_PARAGRAPH_START,
                                    singlePost.getPost().getAuthor().getFullName(),
                                    "</font>",
                                    "<br\\>",
                                    singlePost.getPost().getPostBody() +
                                            "</p>");
                            apiPost.getExtra().setNote(editedPost);
                            //                            apiPost.setPostBody("\\n" + postBody +
                            //                                    "\\n" +
                            //                                    singlePost.getPost().getAuthor().getFullName() +
                            //                                    "\\n" +
                            //                                    singlePost.getPost().getPostBody());

                            mAdapter.notifyItemChanged(pos);
                        }
                    }
                });
    }
}
