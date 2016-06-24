/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.interfaces;

/**
 * Created by mehdok on 6/9/2016.
 */
public interface ReshareBodyClickListener {
    /**
     * in response  to the share text body clicked
     *
     * @param resharePostId id of the reshared post
     */
    void onReshareBodyClicked(String resharePostId);
}
