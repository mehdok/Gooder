/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home;

import android.content.Context;

import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.database.DatabaseHelper;
import com.mehdok.gooder.ui.home.interfaces.PostFunctionListener;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.post.AddPost;
import com.mehdok.gooderapilib.models.post.Like;
import com.mehdok.gooderapilib.models.user.UserInfo;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mehdok on 5/3/2016.
 */
public class PostFunctionHandler {
    private RequestBuilder requestBuilder;
    private QueryBuilder queryBuilder;
    private PostFunctionListener listener;

    public PostFunctionHandler(Context ctx) {
        UserInfo userInfo = DatabaseHelper.getInstance(ctx).getUserInfo();
        requestBuilder = new RequestBuilder();
        queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(userInfo.getUsername());
        try {
            queryBuilder.setPassword(Crypto.getMD5BASE64(
                    new String(Crypto.decrypt(userInfo.getPassword(), ctx))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void likePost(final int position, String pid) {
        requestBuilder.likePost(pid, queryBuilder)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddPost>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onLikeError(position, e);
                        }
                    }

                    @Override
                    public void onNext(AddPost addPost) {
                        if (listener != null) {
                            listener.onLike(position, true);
                        }
                    }
                });
    }

    public void unLikePost(final int position, String pid) {
        requestBuilder.unLikePost(pid, queryBuilder)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddPost>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onLikeError(position, e);
                        }
                    }

                    @Override
                    public void onNext(AddPost addPost) {
                        if (listener != null) {
                            listener.onLike(position, false);
                        }
                    }
                });
    }

    public void starPost(final int position, String pid) {
        requestBuilder.starPost(pid, queryBuilder)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Like>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onStarError(position, e);
                        }
                    }

                    @Override
                    public void onNext(Like like) {
                        if (listener != null) {
                            listener.onStar(position, true);
                        }
                    }
                });
    }

    public void unStarPost(final int position, String pid) {
        requestBuilder.unStarPost(pid, queryBuilder)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Like>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onStarError(position, e);
                        }
                    }

                    @Override
                    public void onNext(Like like) {
                        if (listener != null) {
                            listener.onStar(position, false);
                        }
                    }
                });
    }

    public void setListener(PostFunctionListener listener) {
        this.listener = listener;
    }
}
