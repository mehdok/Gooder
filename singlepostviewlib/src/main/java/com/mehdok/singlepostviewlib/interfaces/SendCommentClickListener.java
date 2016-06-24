/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.interfaces;

/**
 * Created by mehdok on 5/4/2016.
 */
public interface SendCommentClickListener {
    /**
     * in response to  the send comment button
     *
     * @param comment body
     */
    void sendComment(String comment);
}
