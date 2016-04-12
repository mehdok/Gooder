/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mehdok on 4/11/2016.
 */
public class UserInfo implements Parcelable
{
    private String userId;
    private String username;
    private String fullName;
    private String avatar;
    private String about;
    private String web;
    private byte[] password;

    public UserInfo(String userId, String username, String fullName, String avatar, String about, String web, byte[] password)
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

    public byte[] getPassword()
    {
        return password;
    }

    public void setPassword(byte[] password)
    {
        this.password = password;
    }

    private UserInfo(Parcel in)
    {
        this.userId = in.readString();
        this.username = in.readString();
        this.fullName = in.readString();
        this.avatar = in.readString();
        this.about = in.readString();
        this.web = in.readString();
        this.password = new byte[in.readInt()];
        in.readByteArray(this.password);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(userId);
        dest.writeString(username);
        dest.writeString(fullName);
        dest.writeString(avatar);
        dest.writeString(about);
        dest.writeString(web);
        dest.writeInt(password.length);
        dest.writeByteArray(password);
    }

    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>()
    {
        public UserInfo createFromParcel(Parcel in)
        {
            return new UserInfo(in);
        }

        public UserInfo[] newArray(int size)
        {
            return new UserInfo[size];
        }
    };
}
