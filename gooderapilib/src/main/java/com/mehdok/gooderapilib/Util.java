/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib;

import com.mehdok.gooderapilib.models.follow.Followed;
import com.mehdok.gooderapilib.models.follow.FollowedInfo;
import com.mehdok.gooderapilib.models.notification.Notification;
import com.mehdok.gooderapilib.models.notification.NotificationList;

/**
 * Created by mehdok on 5/25/2016.
 */
public class Util {
    public static String getCommaDelimitedUserFromNotif(NotificationList notificationList) {
        StringBuilder result = new StringBuilder();
        for (Notification notification : notificationList.getNotifications()) {
            result.append(notification.getUid());
            result.append(",");
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }

    public static String getCommaDelimitedUserFromFollowed(Followed followed) {
        StringBuilder result = new StringBuilder();
        for (FollowedInfo followedInfo : followed.getFollowedUser()) {
            result.append(followedInfo.getUid());
            result.append(",");
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }
}
