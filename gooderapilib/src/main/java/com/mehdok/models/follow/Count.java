/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.follow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/2/2016.
 */
public class Count {
    @SerializedName("count")
    @Expose
    public String count;

    public String getCount() {
        return count;
    }
}
