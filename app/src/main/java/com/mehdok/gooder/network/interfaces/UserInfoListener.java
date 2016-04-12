/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network.interfaces;

import com.mehdok.gooder.network.model.UserInfo;

/**
 * Created by mehdok on 4/12/2016.
 */
public interface UserInfoListener
{
    void onUserInfoReceive(UserInfo userInfo);
    void onUserInfoFailure(Exception exception);
}
