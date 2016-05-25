/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib;

import com.mehdok.gooderapilib.models.notification.Notification;
import com.mehdok.gooderapilib.models.notification.NotificationList;

/**
 * Created by mehdok on 5/25/2016.
 */
public class Util {
    public static String getCommaDelimitedUser(NotificationList notificationList) {
        StringBuilder result = new StringBuilder();
        for (Notification notification : notificationList.getNotifications()) {
            result.append(notification.getUid());
            result.append(",");
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }
}
