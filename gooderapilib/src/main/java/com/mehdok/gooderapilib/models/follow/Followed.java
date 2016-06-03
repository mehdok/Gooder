/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.follow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mehdok.gooderapilib.models.user.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehdok on 5/2/2016.
 */
public class Followed extends com.mehdok.gooderapilib.models.BaseResponse {
    @SerializedName("msg_data")
    @Expose
    public List<FollowedInfo> followedInfo = new ArrayList<FollowedInfo>();

    private Users users;

    public List<FollowedInfo> getFollowedUser() {
        return followedInfo;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
