/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network.exceptions;

/**
 * Created by mehdok on 4/12/2016.
 */
public class InvalidUserNamePasswordException extends Exception
{
    public InvalidUserNamePasswordException()
    {
        super("Invalid username or password.");
    }
}
