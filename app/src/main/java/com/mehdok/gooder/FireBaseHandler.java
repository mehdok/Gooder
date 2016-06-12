/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by mehdok on 6/12/2016.
 */

public class FireBaseHandler {

    public enum ItemName {
        IN_POST_READ("in_post_read"), // IN : ITEM_NAME
        IN_POST_UNREAD("in_post_unread"),
        IN_POST_LIKED("in_post_liked"),
        IN_POST_UNLIKED("in_post_unliked"),
        IN_POST_SHARE("in_post_share"),
        IN_POST_UNSHARE("in_post_unshare"),
        IN_POST_STAR("in_post_star"),
        IN_POST_UNSTAR("in_post_unstar"),
        IN_POST_EXPAND("in_post_expand"),
        IN_VIEW_PROFILE("in_view_profile"),
        IN_VIEW_FRIENDS_ITEM("in_view_friends_item"), // friends item view
        IN_VIEW_COMMENTS("in_view_comments"), // comments view
        IN_VIEW_STARS("in_view_stars"), // stared item view
        IN_VIEW_NOTIFICATIONS("in_view_notifications"), // notification view
        IN_VIEW_FOLLOWED("in_view_followed"), // people you follow
        IN_VIEW_RECOMMENDED("in_view_recommended"), // recommended items
        IN_ADD_POST("in_add_post"),
        IN_ADD_COMMENT("in_add_comment"),
        IN_USER_FOLLOW("in_user_follow"),
        IN_USER_UNFOLLOW("in_user_unfollow");

        private String mValue;

        private ItemName(String value) {
            mValue = value;
        }

        @Override
        public String toString() {
            return mValue;
        }
    }

    public enum ItemId {
        II_POST_READ("1"), // II : ITEM_ID
        II_POST_UNREAD("2"),
        II_POST_LIKED("3"),
        II_POST_UNLIKED("4"),
        II_POST_SHARE("5"),
        II_POST_UNSHARE("6"),
        II_POST_STAR("7"),
        II_POST_UNSTAR("8"),
        II_POST_EXPAND("9"),
        II_VIEW_PROFILE("10"),
        II_VIEW_FRIENDS_ITEM("11"), // friends item view
        II_VIEW_COMMENTS("12"), // comments view
        II_VIEW_STARS("13"), // stared item view
        II_VIEW_NOTIFICATIONS("14"), // notification view
        II_VIEW_FOLLOWED("15"), // people you follow
        II_VIEW_RECOMMENDED("16"), // recommended items
        II_ADD_POST("17"),
        II_ADD_COMMENT("18"),
        II_USER_FOLLOW("19"),
        II_USER_UNFOLLOW("20");

        private String mValue;

        private ItemId(String value) {
            mValue = value;
        }

        @Override
        public String toString() {
            return mValue;
        }
    }

    public static void sendLogEvent(FirebaseAnalytics firebaseAnalytics,
                                    ItemId itemId,
                                    ItemName itemName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId.toString());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName.toString());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
