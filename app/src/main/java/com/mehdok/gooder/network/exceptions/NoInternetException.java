/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network.exceptions;

/**
 * Created by mehdok on 4/12/2016.
 */
public class NoInternetException extends Exception
{
    public NoInternetException()
    {
        super("There is no active internet connection");
    }
}
