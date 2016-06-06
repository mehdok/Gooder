/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mehdok.gooder.R;
import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.database.DatabaseHelper;
import com.mehdok.gooder.infinitescroll.interfaces.UiToggleListener;
import com.mehdok.gooder.infinitescroll.views.InfiniteRecyclerView;
import com.mehdok.gooder.ui.home.adapters.NotificationAdapter;
import com.mehdok.gooder.ui.home.navigation.MainActivityDelegate;
import com.mehdok.gooder.ui.profile.ProfileActivity;
import com.mehdok.gooder.views.VerticalSpaceItemDecoration;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.notification.Notification;
import com.mehdok.gooderapilib.models.notification.NotificationList;
import com.mehdok.gooderapilib.models.user.UserInfo;
import com.mehdok.singlepostviewlib.interfaces.UserProfileClickListener;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        UserProfileClickListener, UiToggleListener {
    private static NotificationsFragment mInstance;
    private NotificationAdapter mAdapter;
    private ArrayList<Notification> mNotification;
    private ArrayList<UserInfo> mUserInfo;
    private SwipeRefreshLayout refreshLayout;
    private InfiniteRecyclerView mRecyclerView;
    private boolean loadingFlag = false;

    public static NotificationsFragment getInstance() {
        if (mInstance == null) {
            mInstance = new NotificationsFragment();
        }

        return mInstance;
    }

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);

        // setup refresh action
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.notification_refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.refresh_color_1,
                R.color.refresh_color_2,
                R.color.refresh_color_3,
                R.color.refresh_color_4);
        refreshLayout.setOnRefreshListener(this);

        // init recycler
        mRecyclerView = (InfiniteRecyclerView) v.findViewById(R.id.notification_item_recycler);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(
                getActivity().getResources().getDimensionPixelSize(R.dimen.standard_padding)));
        mRecyclerView.setUiToggleListener(this);

        if (mAdapter == null) {
            mNotification = new ArrayList<Notification>();
            mUserInfo = new ArrayList<UserInfo>();
            mAdapter = new NotificationAdapter(getActivity(), mNotification, mUserInfo, this);
            mRecyclerView.setAdapter(mAdapter);

        } else {
            mRecyclerView.setAdapter(mAdapter);
        }

        getData();

        return v;
    }

    public void clearViews() {

    }

    public String getFragmentTag() {
        return null;
    }

    protected void getData() {
        showProgress(true);

        final UserInfo userInfo = DatabaseHelper.getInstance(getActivity()).getUserInfo();
        if (userInfo == null) {
            Toast.makeText(getContext(), R.string.not_logged_in, Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBuilder requestBuilder = new RequestBuilder();
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(userInfo.getUsername());
        try {
            queryBuilder.setPassword(Crypto.getMD5BASE64(
                    new String(Crypto.decrypt(userInfo.getPassword(), getActivity()))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        requestBuilder.getNotification(queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NotificationList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NotificationList notificationList) {
                        showProgress(false);
                        if (notificationList.getNotifications().size() == 0) {
                            Toast.makeText(getActivity(), R.string.notification_no,
                                    Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        mNotification.clear();
                        mUserInfo.clear();
                        mNotification.addAll(notificationList.getNotifications());
                        mUserInfo.addAll(notificationList.getUsers().getUsers());
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    public void refreshData() {
        mNotification.clear();
        mUserInfo.clear();
        getData();
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
        Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
        profileIntent.putExtra(ProfileActivity.PROFILE_USER_ID, userID);
        getActivity().startActivity(profileIntent);
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
