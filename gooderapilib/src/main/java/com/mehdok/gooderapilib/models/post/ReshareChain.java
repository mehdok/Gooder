/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.post;

import java.util.ArrayList;

/**
 * Created by mehdok on 6/8/2016.
 */
public class ReshareChain {

    private int position;
    private ArrayList<APIPost> posts;

    public ReshareChain(int position) {
        this.position = position;
        posts = new ArrayList<>();
    }

    public int getPosition() {
        return position;
    }

    public ArrayList<APIPost> getPosts() {
        return posts;
    }
}
