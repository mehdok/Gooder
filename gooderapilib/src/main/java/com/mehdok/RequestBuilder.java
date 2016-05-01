/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok;

import com.mehdok.models.AccessCode;
import com.mehdok.models.UserInfo;
import com.mehdok.models.post.Posts;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by mehdok on 5/1/2016.
 */
public class RequestBuilder
{
    private GooderInterface mGooderApi;

    public RequestBuilder() {
        mGooderApi = GooderApi.getInstance().getApi();
    }

    /**
     *
     * @param queryBuilder
     * @return  user info
     */
    public Observable<UserInfo> getUserInfo(final QueryBuilder queryBuilder){
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<UserInfo>>() {
                    @Override
                    public Observable<UserInfo> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getUserInfo(queryBuilder.getUserInfoParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    /**
     *
     * @param queryBuilder
     * @return n number of friends item
     */
    public Observable<Posts> getAllFriendsItem(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Posts>>() {
                    @Override
                    public Observable<Posts> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getAllFriendsItem(queryBuilder.getUsersTimeLineParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * If access code is usable return it  otherwise request new one.
     */
    private Observable<String> getAccessCode(QueryBuilder queryBuilder){
        if (GooderApi.lastOperation - System.currentTimeMillis() > GooderApi.ACCESS_CODE_LIMIT) {
            GooderApi.lastOperation = System.currentTimeMillis();
            return Observable.just(GooderApi.mAccessCode);
        }
        else
            return mGooderApi.getAccessCode(queryBuilder.getAccessCodeParams())
                    .flatMap(new Func1<AccessCode, Observable<String>>() {
                        @Override
                        public Observable<String> call(AccessCode accessCode) {
                            GooderApi.mAccessCode = accessCode.getMsgData().getAccessCode();
                            GooderApi.lastOperation = System.currentTimeMillis();
                            return Observable.just(GooderApi.mAccessCode);
                        }
                    });
    }
}
