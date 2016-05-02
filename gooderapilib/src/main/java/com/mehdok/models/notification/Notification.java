/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/2/2016.
 */
public class Notification {
    @SerializedName("nid")
    @Expose
    private String nid;

    @SerializedName("uid")
    @Expose
    private String uid;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("data")
    @Expose
    private NotificationData notificationData;

    public String getNid() {
        return nid;
    }

    public String getUid() {
        return uid;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public NotificationData getNotificationData() {
        return notificationData;
    }
}
