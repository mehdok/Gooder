/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.infinitescroll.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.mehdok.gooder.infinitescroll.interfaces.InfiniteScrollListener;

/**
 * Created by mehdok on 5/3/2016.
 */
public class InfiniteRecyclerView extends RecyclerView {
    private final float SCROLL_AMOUNT = 3 / 4f;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private InfiniteScrollListener listener;

    public InfiniteRecyclerView(Context context) {
        super(context);
        init();
    }

    public InfiniteRecyclerView(Context context,
                                @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InfiniteRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setListener(InfiniteScrollListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        listener = null;
    }

    private void init() {
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = getLayoutManager().getChildCount();
                totalItemCount = getLayoutManager().getItemCount();
                pastVisiblesItems =
                        ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                if ((listener != null) &&
                        ((visibleItemCount + pastVisiblesItems) >=
                                totalItemCount * SCROLL_AMOUNT)) {
                    listener.loadMore();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
