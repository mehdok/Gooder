/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/2/2016.
 */
public class NotificationData {
    @SerializedName("pid")
    @Expose
    private String pid;

    public String getPid() {
        return pid;
    }
}
