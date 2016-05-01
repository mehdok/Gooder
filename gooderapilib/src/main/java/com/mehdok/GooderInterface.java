/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok;

import com.mehdok.models.AccessCode;
import com.mehdok.models.UserInfo;
import com.mehdok.models.post.Posts;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by mehdok on 5/1/2016.
 */
public interface GooderInterface
{
    @FormUrlEncoded
    @POST("?method=get-access-code")
    Observable<AccessCode> getAccessCode(@FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=user-info")
    Observable<UserInfo> getUserInfo(@QueryMap Map<String, String> params, @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=users-timeline")
    Observable<Posts> getAllFriendsItem(@QueryMap Map<String, String> params, @FieldMap Map<String, String> query);

//    @FormUrlEncoded
//    @POST("?method=users-info")
//    Observable<UserInfo> getUsersInfo(@QueryMap Map<String, String> params, @FieldMap Map<String, String> query);
}
