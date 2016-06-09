/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mehdok.gooder.Globals;
import com.mehdok.gooder.R;
import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.database.DatabaseHelper;
import com.mehdok.gooder.infinitescroll.interfaces.InfiniteScrollListener;
import com.mehdok.gooder.infinitescroll.views.InfiniteRecyclerView;
import com.mehdok.gooder.ui.home.adapters.SinglePostAdapter;
import com.mehdok.gooder.ui.home.models.ParcelablePost;
import com.mehdok.gooder.ui.profile.views.FollowButton;
import com.mehdok.gooder.utils.ReshareUtil;
import com.mehdok.gooder.utils.Util;
import com.mehdok.gooder.views.VerticalSpaceItemDecoration;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.follow.FollowResponse;
import com.mehdok.gooderapilib.models.post.APIPost;
import com.mehdok.gooderapilib.models.post.APIPosts;
import com.mehdok.gooderapilib.models.post.ReshareChain;
import com.mehdok.gooderapilib.models.user.UserInfo;
import com.mehdok.singlepostviewlib.utils.GlideHelper;
import com.mehdok.singlepostviewlib.views.PostTextView;

import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProfileActivity extends AppCompatActivity implements InfiniteScrollListener,
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, ReshareUtil.ReshareChainListener {

    private final String UNI_LTR = "\u200e";
    public static final String PROFILE_USER_ID = "profile_user_id";
    private UserInfo mUserInfo;
    private String currentUserId;
    private boolean loadingFlag = false;
    private boolean reachEndOfPosts = false;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FollowButton followButton;
    private AppCompatImageView imgUser;
    private InfiniteRecyclerView mRecyclerView;
    private SinglePostAdapter mAdapter;
    private ArrayList<APIPost> mPosts;
    private CoordinatorLayout mRootLayout;
    private PostTextView tvAboutMe;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // setup refresh layout
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.profile_refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.refresh_color_1,
                R.color.refresh_color_2,
                R.color.refresh_color_3,
                R.color.refresh_color_4);
        refreshLayout.setOnRefreshListener(this);

        // setup toolbar
        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get user info
        mUserInfo = DatabaseHelper.getInstance(this).getUserInfo();
        if (mUserInfo == null) {
            Toast.makeText(this, R.string.not_logged_in, Toast.LENGTH_SHORT).show();
            return;
        }
        // get extra
        currentUserId = getIntent().getStringExtra(PROFILE_USER_ID);

        // find views
        mRootLayout = (CoordinatorLayout) findViewById(R.id.profile_root_layout);
        imgUser = (AppCompatImageView) findViewById(R.id.profile_photo);
        followButton = (FollowButton) findViewById(R.id.profile_follow_button);
        collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.profile_toolbar_layout);
        tvAboutMe = (PostTextView) findViewById(R.id.profile_about_text);

        mRecyclerView = (InfiniteRecyclerView) findViewById(R.id.profile_recycler_view);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.standard_padding)));
        mRecyclerView.setInfiniteScrollListener(this);

        if (mAdapter == null) {
            mPosts = new ArrayList<>();
            mAdapter = new SinglePostAdapter(this, mPosts, null);
            mRecyclerView.setAdapter(mAdapter);

            showProfile();
        } else {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        fillUserInfo(mUserInfo.getFullname(), mUserInfo.getAvatar(), mUserInfo.getAbout());
        getUserPosts();
    }

    private void otherUI() {
        collapsingToolbarLayout.setTitle(" ");
        getUserInfo();
        getFollowedInfo();
        getUserPosts();
    }

    private void fillUserInfo(String userName, String avatar, String about) {
        collapsingToolbarLayout.setTitle(UNI_LTR + userName);
        tvAboutMe.setText(about);
        loadUserImage(avatar);
    }

    private void loadUserImage(String avatar) {
        GlideHelper.loadProfileImage(this, avatar, imgUser, null);
    }

    private void getUserPosts() {
        // return if there is no more posts
        if (reachEndOfPosts) return;

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
                        showBugSnackBar(e);
                    }

                    @Override
                    public void onNext(APIPosts posts) {
                        showProgress(false);
                        if (posts != null) {
                            // if there is no more posts, show message and return
                            if (posts.getPosts().size() == 0) {
                                reachEndOfPosts = true;
                                showSimpleMessage(getString(R.string.last_post));
                                return;
                            }

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

    private void showProgress(final boolean show) {
        loadingFlag = show;

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(show);
            }
        });
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
        /*reshareUtil.setListener(this);
        reshareUtil.checkForReshares(mPosts, from, queryBuilder);*/
        reshareUtil.setReshareChainListener(this);
        reshareUtil.getReshareChain(mPosts, from, queryBuilder);
    }

    private void getUserInfo() {
        RequestBuilder requestBuilder = new RequestBuilder();
        QueryBuilder queryBuilder = getQueryBuilder();
        queryBuilder.setUid(currentUserId);

        requestBuilder.getUserInfo(queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showBugSnackBar(e);
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        fillUserInfo(userInfo.getFullname(), userInfo.getAvatar(),
                                userInfo.getAbout());
                    }
                });
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
                        Util.sendBugReport(ProfileActivity.this,
                                getString(R.string.bug_email_subject),
                                getString(R.string.bug_email_context));
                    }
                }).show();
    }

    private void getFollowedInfo() {
        RequestBuilder requestBuilder = new RequestBuilder();
        requestBuilder.isUserFollowed(Integer.valueOf(currentUserId), getQueryBuilder())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FollowResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(FollowResponse followResponse) {
                        showFollowButton(followResponse.getMsgData());
                    }
                });
    }

    private void showFollowButton(boolean followed) {
        followButton.setVisibility(View.VISIBLE);
        followButton.setFollow(followed);
        followButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.profile_follow_button) {
            if (followButton.isFollowed()) {
                unfollowUser();
            } else {
                followUser();
            }
        }
    }

    private void unfollowUser() {
        followButton.setFollow(false);
        RequestBuilder requestBuilder = new RequestBuilder();
        requestBuilder.unFollowUser(Integer.valueOf(currentUserId), getQueryBuilder())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FollowResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(FollowResponse followResponse) {
                        if (followResponse.getMsgData()) {
                            Toast.makeText(ProfileActivity.this, R.string.user_unfollowed,
                                    Toast.LENGTH_SHORT).show();
                            followButton.setFollow(false);
                        } else {
                            Toast.makeText(ProfileActivity.this, R.string.can_not_do,
                                    Toast.LENGTH_SHORT).show();
                            followButton.setFollow(true);
                        }
                    }
                });
    }

    private void followUser() {
        followButton.setFollow(true);
        RequestBuilder requestBuilder = new RequestBuilder();
        requestBuilder.followUser(Integer.valueOf(currentUserId), getQueryBuilder())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FollowResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(FollowResponse followResponse) {
                        if (followResponse.getMsgData()) {
                            Toast.makeText(ProfileActivity.this, R.string.user_followed,
                                    Toast.LENGTH_SHORT).show();
                            followButton.setFollow(true);
                        } else {
                            Toast.makeText(ProfileActivity.this, R.string.can_not_do,
                                    Toast.LENGTH_SHORT).show();
                            followButton.setFollow(false);
                        }
                    }
                });
    }

    private QueryBuilder getQueryBuilder() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(mUserInfo.getUsername());
        try {
            queryBuilder.setPassword(Crypto.getMD5BASE64(
                    new String(Crypto.decrypt(mUserInfo.getPassword(), this))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return queryBuilder;
    }

    public void showSimpleMessage(String str) {
        Snackbar.make(mRootLayout, str, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onRefresh() {
        reachEndOfPosts = false;
        mPosts.clear();
        getUserPosts();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(postContentChangeReceiver, new IntentFilter(
                        Globals.POST_CONTENT_CHANGED));
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

    @Override
    public void onReshareChainFetched(ReshareChain reshareChain) {
        /*String log = "pos: " +
                reshareChain.getPosition() +
                " -- id: " +
                mPosts.get(reshareChain.getPosition()).getPid() +
                "\n";
        for (APIPost apiPost : reshareChain.getPosts()) {
            log += "post id: " + apiPost.getPid() + "\n";
        }

        Logger.e("onReshareChainFetched: " + log);*/
        mPosts.get(reshareChain.getPosition()).setReshareChains(reshareChain.getPosts());
        mAdapter.notifyItemChanged(reshareChain.getPosition());
    }
}
