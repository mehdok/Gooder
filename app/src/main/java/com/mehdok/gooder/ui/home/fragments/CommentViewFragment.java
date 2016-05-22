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
public class CommentViewFragment extends BaseFragment {
    private static CommentViewFragment mInstance;

    public CommentViewFragment() {
        // Required empty public constructor
    }

    public static CommentViewFragment getInstance() {
        if (mInstance == null) {
            mInstance = new CommentViewFragment();
        }

        return mInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment_view, container, false);
    }

    @Override
    public void clearViews() {

    }

    @Override
    public String getFragmentTag() {
        return null;
    }

    @Override
    public void refreshData() {

    }

}
