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
import com.mehdok.gooder.infinitescroll.interfaces.UiToggleListener;

/**
 * Created by mehdok on 5/3/2016.
 */
public class InfiniteRecyclerView extends RecyclerView {
    private final float SCROLL_AMOUNT = 3 / 4f;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private InfiniteScrollListener infiniteScrollListener;
    private UiToggleListener uiToggleListener;

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

    public void setInfiniteScrollListener(InfiniteScrollListener infiniteScrollListener) {
        this.infiniteScrollListener = infiniteScrollListener;
    }

    public void removeInfiniteScrollListener() {
        infiniteScrollListener = null;
    }

    public void setUiToggleListener(
            UiToggleListener uiToggleListener) {
        this.uiToggleListener = uiToggleListener;
    }

    public void removeUiToggleListener() {
        uiToggleListener = null;
    }

    private void init() {
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (uiToggleListener != null) {
                    if (dy > 0) {
                        uiToggleListener.hide();
                    } else {
                        uiToggleListener.show();
                    }
                }

                visibleItemCount = getLayoutManager().getChildCount();
                totalItemCount = getLayoutManager().getItemCount();
                pastVisiblesItems =
                        ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                if ((infiniteScrollListener != null) &&
                        ((visibleItemCount + pastVisiblesItems) >=
                                totalItemCount * SCROLL_AMOUNT)) {
                    infiniteScrollListener.loadMore();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
