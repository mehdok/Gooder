/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.follow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/2/2016.
 */
public class FollowedInfo {

    @SerializedName("uid")
    @Expose
    private Integer uid;

    @SerializedName("gid")
    @Expose
    private Integer gid;

    @SerializedName("unreads")
    @Expose
    private Integer unreads;

    public Integer getUid() {
        return uid;
    }

    public Integer getGid() {
        return gid;
    }

    public Integer getUnreads() {
        return unreads;
    }
}
