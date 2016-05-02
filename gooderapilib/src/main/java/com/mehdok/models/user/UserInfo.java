/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/1/2016.
 */
public class UserInfo {
    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("fullname")
    @Expose
    public String fullname;
    @SerializedName("avatar")
    @Expose
    public String avatar;
    @SerializedName("about")
    @Expose
    public String about;
    @SerializedName("web")
    @Expose
    public String web;

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAbout() {
        return about;
    }

    public String getWeb() {
        return web;
    }
}
