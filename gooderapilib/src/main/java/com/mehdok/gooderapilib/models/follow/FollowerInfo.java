/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.follow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/2/2016.
 */
public class FollowerInfo {
    @SerializedName("uid")
    @Expose
    public String uid;

    public String getUid() {
        return uid;
    }
}
