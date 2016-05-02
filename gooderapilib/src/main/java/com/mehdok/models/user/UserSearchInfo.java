/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mehdok.models.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehdok on 5/2/2016.
 */
public class UserSearchInfo extends BaseResponse {
    @SerializedName("msg_data")
    @Expose
    public List<UserInfo> users = new ArrayList<UserInfo>();

    public List<UserInfo> getUsers() {
        return users;
    }
}
