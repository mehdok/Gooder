/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.follow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mehdok.models.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehdok on 5/2/2016.
 */
public class Followed extends BaseResponse {
    @SerializedName("msg_data")
    @Expose
    public List<FollowedInfo> followedInfo = new ArrayList<FollowedInfo>();

    public List<FollowedInfo> getFollowedUser() {
        return followedInfo;
    }
}
