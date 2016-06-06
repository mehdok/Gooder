/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.followed;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.mehdok.gooder.R;
import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.database.DatabaseHelper;
import com.mehdok.gooder.ui.followed.adapters.FollowedAdapter;
import com.mehdok.gooder.ui.profile.ProfileActivity;
import com.mehdok.gooder.utils.Util;
import com.mehdok.gooder.views.VerticalSpaceItemDecoration;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.follow.Followed;
import com.mehdok.gooderapilib.models.follow.FollowedInfo;
import com.mehdok.gooderapilib.models.user.UserInfo;
import com.mehdok.singlepostviewlib.interfaces.UserProfileClickListener;

import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FollowedActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, UserProfileClickListener {

    private SwipeRefreshLayout refreshLayout;
    private UserInfo mUserInfo;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<FollowedInfo> followedInfos;
    private ArrayList<UserInfo> userInfos;
    private boolean loadingFlag = false;
    private FollowedAdapter mAdapter;
    private CoordinatorLayout mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followed);

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.followed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRootLayout = (CoordinatorLayout) findViewById(R.id.followed_root_layout);

        // setup refresh layout
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.followed_refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.refresh_color_1,
                R.color.refresh_color_2,
                R.color.refresh_color_3,
                R.color.refresh_color_4);
        refreshLayout.setOnRefreshListener(this);

        // get user info
        mUserInfo = DatabaseHelper.getInstance(this).getUserInfo();
        if (mUserInfo == null) {
            Toast.makeText(this, R.string.not_logged_in, Toast.LENGTH_SHORT).show();
            return;
        }
        // setup recycler
        mRecyclerView = (RecyclerView) findViewById(R.id.followed_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.standard_padding)));
        followedInfos = new ArrayList<>();
        userInfos = new ArrayList<>();
        mAdapter = new FollowedAdapter(this, followedInfos, userInfos, this);
        mRecyclerView.setAdapter(mAdapter);

        getData();
    }

    @Override
    public void onRefresh() {
        followedInfos.clear();
        userInfos.clear();
        getData();
    }

    private void getData() {
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

        requestBuilder.getFollowedUsers(queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Followed>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showProgress(false);
                        showBugSnackBar(e);
                    }

                    @Override
                    public void onNext(Followed followed) {
                        showProgress(false);
                        followedInfos.clear();
                        userInfos.clear();
                        followedInfos.addAll(followed.getFollowedUser());
                        userInfos.addAll(followed.getUsers().getUsers());
                        mAdapter.notifyDataSetChanged();
                    }
                });
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

    @Override
    public void showUserProfile(String userID) {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra(ProfileActivity.PROFILE_USER_ID, userID);
        startActivity(profileIntent);
    }

    public void showBugSnackBar(Throwable e) {
        e.printStackTrace();
        String error;
        if (e instanceof HttpException) {
            error = ((HttpException) e).response().body().toString();
        } else {
            error = e.getMessage();
        }
        Snackbar.make(mRootLayout, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.send_report, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.sendBugReport(FollowedActivity.this,
                                getString(R.string.bug_email_subject),
                                getString(R.string.bug_email_context));
                    }
                }).show();
    }
}
