/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network.model;

/**
 * Created by mehdok on 4/11/2016.
 */
public class UserInfo
{
    private String userId;
    private String username;
    private String fullName;
    private String avatar;
    private String about;
    private String web;

    private String password;

    public UserInfo(String userId, String username, String fullName, String avatar, String about, String web, String password)
    {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.avatar = avatar;
        this.about = about;
        this.web = web;
        this.password = password;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getUsername()
    {
        return username;
    }

    public String getFullName()
    {
        return fullName;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public String getAbout()
    {
        return about;
    }

    public String getWeb()
    {
        return web;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
