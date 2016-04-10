/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehdok.gooder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsItemFragment extends Fragment
{
    private static FriendsItemFragment mInstance;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends_item, container, false);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        mInstance = null;
    }
}
