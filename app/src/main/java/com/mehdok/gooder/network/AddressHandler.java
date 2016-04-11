/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network;

/**
 * Created by mehdok on 4/11/2016.
 */
public class AddressHandler
{
    /**
     * @POST client_id , username , password
     */
    public static String getUriAccessCode()
    {
        return "http://gooder.in/api/?method=get-access-code";
    }

    /**
     * using with parameters client_id via POST
     * @optional GET uid
     */
    public static String getUriUserInfo()
    {
        return "http://gooder.in/api/?method=user-info";
    }
}
