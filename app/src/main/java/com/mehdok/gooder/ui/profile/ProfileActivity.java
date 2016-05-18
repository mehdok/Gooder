/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.profile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mehdok.gooder.R;
import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.database.DatabaseHelper;
import com.mehdok.gooder.infinitescroll.interfaces.InfiniteScrollListener;
import com.mehdok.gooder.infinitescroll.views.InfiniteRecyclerView;
import com.mehdok.gooder.ui.home.adapters.SinglePostAdapter;
import com.mehdok.gooder.ui.home.navigation.MainActivityDelegate;
import com.mehdok.gooder.ui.profile.views.FollowButton;
import com.mehdok.gooder.utils.ReshareUtil;
import com.mehdok.gooder.views.VerticalSpaceItemDecoration;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.post.APIPost;
import com.mehdok.gooderapilib.models.post.APIPosts;
import com.mehdok.gooderapilib.models.user.UserInfo;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProfileActivity extends AppCompatActivity implements InfiniteScrollListener,
        ReshareUtil.ReshareUpdateListener {

    private final String UNI_LTR = "\u200e";
    public static final String PROFILE_USER_ID = "profile_user_id";
    private UserInfo mUserInfo;
    private String currentUserId;
    private boolean loadingFlag = false;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FollowButton followButton;
    private AppCompatImageView imgUser;
    private InfiniteRecyclerView mRecyclerView;
    private ProgressBar mProgress;
    private SinglePostAdapter mAdapter;
    private ArrayList<APIPost> mPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // setup toolbar
        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get user info
        mUserInfo = DatabaseHelper.getInstance(this).getUserInfo();

        // get extra
        currentUserId = getIntent().getStringExtra(PROFILE_USER_ID);

        // find views
        imgUser = (AppCompatImageView) findViewById(R.id.profile_photo);
        followButton = (FollowButton) findViewById(R.id.profile_follow_button);
        collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.profile_toolbar_layout);

        mRecyclerView = (InfiniteRecyclerView) findViewById(R.id.profile_recycler_view);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.standard_padding)));
        mRecyclerView.setInfiniteScrollListener(this);

        mProgress = (ProgressBar) findViewById(R.id.profile_item_progress);

        if (mAdapter == null) {
            mPosts = new ArrayList<>();
            mAdapter = new SinglePostAdapter(this, mPosts, null);
            mRecyclerView.setAdapter(mAdapter);

            showProfile();
        } else {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void showProfile() {
        if (isOwnerProfile()) {
            ownerUI();
        } else {
            otherUI();
        }
    }

    private boolean isOwnerProfile() {
        if (currentUserId == null || currentUserId.isEmpty()) {
            currentUserId = mUserInfo.getUid();
            return true;
        }

        return mUserInfo.getUid().equals(currentUserId);
    }

    private void ownerUI() {
        followButton.setVisibility(View.GONE);
        fillUserInfo(mUserInfo.getFullname(), mUserInfo.getAvatar());
        getUserPosts();
    }

    private void otherUI() {
        collapsingToolbarLayout.setTitle("");
        //TODO get UserInfo
        // TODO get follow info
        //TODO get User post
    }

    private void fillUserInfo(String userName, String avatar) {
        collapsingToolbarLayout.setTitle(UNI_LTR + userName);
        loadUserImage(avatar);
    }

    private void loadUserImage(String avatar) {
        Glide
                .with(this)
                .load(avatar)
                .asBitmap()
                .centerCrop()
                .placeholder(R.mipmap.logo)
                .into(new BitmapImageViewTarget(imgUser) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imgUser.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    private void getUserPosts() {
        showProgress(true);

        RequestBuilder requestBuilder = new RequestBuilder();
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(mUserInfo.getUsername());
        try {
            queryBuilder.setPassword(Crypto.getMD5BASE64(
                    new String(Crypto.decrypt(mUserInfo.getPassword(), this))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        queryBuilder.setStart(mPosts.size());
        queryBuilder.setUid(currentUserId);

        requestBuilder.getUserPosts(queryBuilder)
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

                            checkForReshares(mPosts.size() - posts.getPosts().size(), mUserInfo);
                        }
                    }
                });
    }

    @Override
    public void loadMore() {
        if (!loadingFlag) {
            getUserPosts();
        }
    }

    private void showProgress(boolean show) {
        loadingFlag = show;

        if (show) {
            mProgress.setVisibility(View.VISIBLE);
        } else {
            mProgress.setVisibility(View.INVISIBLE);
        }
    }

    private void checkForReshares(int from, UserInfo userInfo) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(userInfo.getUsername());
        try {
            queryBuilder.setPassword(Crypto.getMD5BASE64(
                    new String(Crypto.decrypt(userInfo.getPassword(), this))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ReshareUtil reshareUtil = new ReshareUtil();
        reshareUtil.setListener(this);
        reshareUtil.checkForReshares(mPosts, from, queryBuilder);
    }

    @Override
    public void ResharePostFetched(int position) {
        mAdapter.notifyItemChanged(position);
    }
}
