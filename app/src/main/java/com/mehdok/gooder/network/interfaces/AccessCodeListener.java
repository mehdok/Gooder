/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network.interfaces;

/**
 * Created by mehdok on 4/11/2016.
 */
public interface AccessCodeListener
{
    void onAccessCodeReceive(String accessCode);
    void onAccessCodeFailure(Exception exception);
}
