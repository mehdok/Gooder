/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/1/2016.
 */
public class Author {
    @SerializedName("uid") @Expose private String uid;
    @SerializedName("fullname") @Expose private String fullName;

    public String getUid() {
        return uid;
    }

    public String getFullName() {
        return fullName;
    }
}
