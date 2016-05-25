/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mehdok.gooderapilib.models.user.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehdok on 5/2/2016.
 */
public class NotificationList extends com.mehdok.gooderapilib.models.BaseResponse {
    @SerializedName("msg_data")
    @Expose
    public List<Notification> notifications = new ArrayList<Notification>();

    private Users users;

    public List<Notification> getNotifications() {
        return notifications;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
