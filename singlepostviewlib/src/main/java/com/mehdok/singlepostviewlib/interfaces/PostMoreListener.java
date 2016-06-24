/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.interfaces;

/**
 * Created by mehdok on 5/21/2016.
 */
public interface PostMoreListener {
    /**
     * copy post id into the clicpboard, eg. for mentioning
     *
     * @param pos of the post
     */
    void copyPostId(int pos);

    /**
     * copy post author id into the clipboard
     * @param pos of the post
     */
    void copyPostAuthorId(int pos);

    /**
     * copy post body into the clipboard
     * @param pos of th post
     */
    void copyPostText(int pos);
}
