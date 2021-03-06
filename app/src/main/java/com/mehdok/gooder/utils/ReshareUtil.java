/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.utils;

import android.widget.LinearLayout;

import com.mehdok.gooder.R;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.post.APIPost;
import com.mehdok.gooderapilib.models.post.ReshareChain;
import com.mehdok.singlepostviewlib.interfaces.ReshareBodyClickListener;
import com.mehdok.singlepostviewlib.interfaces.UserProfileClickListener;
import com.mehdok.singlepostviewlib.utils.PrettySpann;
import com.mehdok.singlepostviewlib.views.ResharePostView;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mehdok on 5/18/2016.
 */
public class ReshareUtil {

    public interface ReshareChainListener {
        void onReshareChainFetched(ReshareChain reshareChain);
    }

    private ReshareChainListener reshareChainListener;

    public void setReshareChainListener(
            ReshareChainListener reshareChainListener) {
        this.reshareChainListener = reshareChainListener;
    }

    public void getReshareChain(ArrayList<APIPost> posts, int from, QueryBuilder queryBuilder) {
        RequestBuilder requestBuilder = new RequestBuilder();
        for (int i = from; i < posts.size(); i++) {
            if (posts.get(i).getParentPid() != null &&
                    !posts.get(i).getParentPid().equals("0")) {
                requestBuilder.getReshareChain(new ReshareChain(i), posts.get(i).getParentPid(),
                        queryBuilder)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ReshareChain>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(ReshareChain chain) {
                                if (reshareChainListener != null) {
                                    reshareChainListener.onReshareChainFetched(chain);
                                }
                            }
                        });
            }
        }
    }

    public static void getReshareChainView(LinearLayout rootLayout,
                                           ArrayList<APIPost> reshareChain, int textLimit,
                                           PrettySpann.TagClickListener tagClickListener,
                                           UserProfileClickListener userProfileClickListener,
                                           ReshareBodyClickListener reshareBodyClickListener) {
        //        if (rootLayout.getChildCount() <= 0) { // there is already an item, don't recreate views
            for (int i = 0; i < reshareChain.size(); i++) {
                ResharePostView resharePostView =
                        new ResharePostView(rootLayout.getContext(), reshareChain.get(i).getTime(),
                                reshareChain.get(i).getSharesCount(),
                                reshareChain.get(i).getLikeCounts(),
                                reshareChain.get(i).getAuthor().getFullName(),
                                reshareChain.get(i).getAuthor().getUid(),
                                reshareChain.get(i).getTitle(),
                                textLimit == -1 ? reshareChain.get(i).getPostBody() :
                                        MehdokTextUtil.getLimitedText(
                                                reshareChain.get(i).getPostBody(),
                                                MehdokTextUtil.BODY_COUNT),
                                reshareChain.get(i).getPid(),
                                tagClickListener,
                                userProfileClickListener,
                                reshareBodyClickListener);

                resharePostView.changeBackgroundColor(
                        ColorUtil.getDarkerColor(rootLayout.getContext().getResources().getColor(
                                R.color.reshare_color), i));

                rootLayout.addView(resharePostView);
            }
        //        }
    }
}
