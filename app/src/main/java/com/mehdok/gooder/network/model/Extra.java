/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network.model;

/**
 * Created by mehdok on 4/13/2016.
 */
public class Extra
{
    private String note;
    private String url;

    public Extra(String note, String url)
    {
        this.note = note != null ? note : "";
        this.url = url != null ? url : "";
    }

    public String getNote()
    {
        return note;
    }

    public String getUrl()
    {
        return url;
    }
}
