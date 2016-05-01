/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/1/2016.
 */
public class AccessCodeData
{
    @SerializedName("access_code")
    @Expose
    public String accessCode;

    public String getAccessCode()
    {
        return accessCode;
    }
}
