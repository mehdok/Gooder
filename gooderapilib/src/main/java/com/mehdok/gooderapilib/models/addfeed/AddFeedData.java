/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.addfeed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/2/2016.
 */
public class AddFeedData {
    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("title")
    @Expose
    public String title;

    public String getUid() {
        return uid;
    }

    public String getTitle() {
        return title;
    }
}
