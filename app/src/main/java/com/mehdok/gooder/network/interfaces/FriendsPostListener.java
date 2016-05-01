/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network.interfaces;

import com.mehdok.gooder.network.model.Post;

import java.util.ArrayList;

/**
 * Created by mehdok on 4/13/2016.
 */
public interface FriendsPostListener {
    void onFriendsPostReceive(ArrayList<Post> posts);

    void onFriendsPostFailure(Exception exception);
}
