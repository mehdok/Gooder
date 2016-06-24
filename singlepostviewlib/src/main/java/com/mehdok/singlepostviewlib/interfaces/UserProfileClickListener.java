/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.interfaces;

/**
 * Created by mehdok on 5/18/2016.
 */
public interface UserProfileClickListener {
    /**
     * in response to click on user photo or name for showing his profile
     *
     * @param userID
     */
    void showUserProfile(String userID);
}
