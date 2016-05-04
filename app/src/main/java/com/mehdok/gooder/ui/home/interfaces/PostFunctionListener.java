/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.interfaces;

import com.mehdok.gooderapilib.models.post.AddPost;

/**
 * Created by mehdok on 5/3/2016.
 */
public interface PostFunctionListener {
    void onLike(int position, boolean like);
    void onStar(int position, boolean star);
    void onLikeError(int position, Throwable e);
    void onStarError(int position, Throwable e);

    void onReShare(int position, AddPost result);

    void onReShareError(int position, Throwable e);
}
