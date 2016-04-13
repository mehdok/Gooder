/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehdok.gooder.R;
import com.mehdok.gooder.ui.home.MainActivity;

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

        String accessCode = getArguments().getString(MainActivity.TAG_ACCESS_CODE);
        Log.e("onCreateView", "accessCode: " + accessCode);

        return inflater.inflate(R.layout.fragment_friends_item, container, false);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        mInstance = null;
    }
}
