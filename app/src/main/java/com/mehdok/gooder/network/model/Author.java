/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network.model;

/**
 * Created by mehdok on 4/13/2016.
 */
public class Author
{
    private String uid;
    private String fullName;

    public Author(String uid, String fullName)
    {
        this.uid = uid != null ? uid : "";
        this.fullName = fullName != null ? fullName : "";
    }

    public String getUid()
    {
        return uid;
    }

    public String getFullName()
    {
        return fullName;
    }
}
