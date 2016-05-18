/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.utils;

import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.post.APIPost;
import com.mehdok.gooderapilib.models.post.SinglePost;
import com.mehdok.singlepostviewlib.utils.PrettySpann;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mehdok on 5/18/2016.
 */
public class ReshareUtil {

    private ReshareUpdateListener listener;

    public void setListener(ReshareUpdateListener listener) {
        this.listener = listener;
    }

    public void checkForReshares(ArrayList<APIPost> posts, int from, QueryBuilder queryBuilder) {
        for (int i = from; i < posts.size(); i++) {
            if (!posts.get(i).getParentPid().equals("0")) {
                getResharedPost(i, posts.get(i).getPostBody(), posts.get(i).getParentPid(), 0,
                        queryBuilder, posts);
            }
        }
    }

    private void getResharedPost(final int pos, final String postBody,
                                 String parentId, int count, final QueryBuilder queryBuilder,
                                 final ArrayList<APIPost> posts) {
        ++count;

        RequestBuilder requestBuilder = new RequestBuilder();

        final int finalCount = count;
        requestBuilder.getPost(parentId, queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SinglePost>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(SinglePost singlePost) {
                        if (!singlePost.getPost().getParentPid().equals("0")) {
                            String body = postBody +
                                    PrettySpann.SHARE_PARAGRAPH_START +
                                    singlePost.getPost().getAuthor().getFullName() +
                                    "</font>" +
                                    "<br\\>" +
                                    singlePost.getPost().getPostBody() +
                                    "</p><br\\><br\\>";

                            getResharedPost(pos,
                                    body,
                                    singlePost.getPost().getParentPid(), finalCount + 1,
                                    queryBuilder, posts);
                        } else {
                            APIPost apiPost = posts.get(pos);
                            String editedPost = "";

                            editedPost += String.format("%s%s%s%s%s",
                                    PrettySpann.SHARE_PARAGRAPH_START,
                                    singlePost.getPost().getAuthor().getFullName(),
                                    "</font>",
                                    "<br\\>",
                                    singlePost.getPost().getPostBody() +
                                            "</p>");
                            apiPost.getExtra().setNote(editedPost);

                            if (listener != null) {
                                listener.ResharePostFetched(pos);
                            }
                        }
                    }
                });
    }

    public interface ReshareUpdateListener {
        void ResharePostFetched(int position);
    }
}
