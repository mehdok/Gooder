/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehdok on 5/2/2016.
 */
public class Users {
    @SerializedName("users")
    @Expose
    public List<UserInfo> users = new ArrayList<UserInfo>();
    @SerializedName("start")
    @Expose
    public Integer start;
    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("total")
    @Expose
    public Integer total;

    public List<UserInfo> getUsers() {
        return users;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getTotal() {
        return total;
    }
}
