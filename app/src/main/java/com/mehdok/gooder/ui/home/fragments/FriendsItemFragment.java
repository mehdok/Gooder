/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mehdok.gooder.R;
import com.mehdok.gooder.network.JsonHandler;
import com.mehdok.gooder.network.exceptions.NoInternetException;
import com.mehdok.gooder.network.interfaces.FriendsPostListener;
import com.mehdok.gooder.network.model.Post;
import com.mehdok.gooder.ui.home.MainActivity;
import com.mehdok.gooder.ui.home.adapters.SinglePostAdapter;
import com.mehdok.gooder.ui.home.navigation.MainActivityDelegate;
import com.mehdok.gooder.views.VerticalSpaceItemDecoration;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsItemFragment extends Fragment implements FriendsPostListener
{
    private static FriendsItemFragment mInstance;
    private String accessCode;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgress;
    private SinglePostAdapter mAdapter;
    private ArrayList<Post> mPosts;

    public static FriendsItemFragment getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new FriendsItemFragment();
        }

        return mInstance;
    }

    public FriendsItemFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        accessCode = getArguments().getString(MainActivity.TAG_ACCESS_CODE);

        View v = inflater.inflate(R.layout.fragment_friends_item, container, false);

        // init recycler
        mRecyclerView = (RecyclerView)v.findViewById(R.id.friends_item_recycler);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(getActivity().getResources().getDimensionPixelSize(R.dimen.standard_padding)));

        if (mAdapter == null)
        {
            mPosts = new ArrayList<>();
            mAdapter = new SinglePostAdapter(mPosts);
            mRecyclerView.setAdapter(mAdapter);

            //TODO set recycler listener

            mProgress = (ProgressBar)v.findViewById(R.id.friends_item_progress);

            getData();
        }
        else
        {
            mRecyclerView.setAdapter(mAdapter);
        }

        return v;
    }

    private void showProgress(boolean show)
    {
        if (show)
            mProgress.setVisibility(View.VISIBLE);
        else
            mProgress.setVisibility(View.INVISIBLE);
    }

    private void getData()
    {
        showProgress(true);

        //TODO do something about this fucking access code
        JsonHandler.getInstance().requestFriendsPost(getActivity(), accessCode, null, mPosts.size(), 0, 0);
    }

    @Override
    public void onFriendsPostReceive(ArrayList<Post> posts)
    {
        showProgress(false);

        if (posts != null)
        {
            mPosts.addAll(posts);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFriendsPostFailure(Exception exception)
    {
        showProgress(false);

        //TODO handle other exceptions
        if (exception instanceof NoInternetException)
        {
            MainActivityDelegate.getInstance().getActivity().
                    showNoInternetError((NoInternetException) exception);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        JsonHandler.getInstance().setFriendsPostListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        JsonHandler.getInstance().removeFriendsPostListener();
    }

}
