/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.network.exceptions.InvalidUserNamePasswordException;
import com.mehdok.gooder.network.exceptions.NoInternetException;
import com.mehdok.gooder.network.exceptions.UserInfoException;
import com.mehdok.gooder.network.handler.JsonThreadHandler;
import com.mehdok.gooder.network.interfaces.AccessCodeListener;
import com.mehdok.gooder.network.interfaces.UserInfoListener;
import com.mehdok.gooder.network.model.UserInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by mehdok on 4/11/2016.
 */
public class JsonHandler
{
    private static JsonHandler mInstance;
    private JsonThreadHandler mHandler;
    private JsonParser parser;

    private final int MSG_ACCESS_CODE_RECEIVED = 1;
    private final int MSG_USER_INFO_RECEIVED = 2;

    private ArrayList<AccessCodeListener> accessCodeListener;
    private UserInfoListener userInfoListener;

    public static JsonHandler getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new JsonHandler();
        }

        return mInstance;
    }

    private JsonHandler()
    {
        mHandler = new JsonThreadHandler(this);
        parser = new JsonParser();
        accessCodeListener = new ArrayList<>();
    }

    /**
     * handle received message from thread
     * @param msg
     */
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
            case MSG_ACCESS_CODE_RECEIVED:
                if (msg.arg1 > 0)
                {
                    String parsedJson = (String)msg.obj;
                    for (AccessCodeListener al : accessCodeListener)
                        al.onAccessCodeReceive(parsedJson);
                }
                else
                {
                    Exception exception = (Exception) msg.obj;
                    for (AccessCodeListener al : accessCodeListener)
                        al.onAccessCodeFailure(exception);
                }
                break;
            case MSG_USER_INFO_RECEIVED:
                UserInfo userInfo = (UserInfo)msg.obj;
                if (userInfoListener != null)
                {
                    if (userInfo != null)
                        userInfoListener.onUserInfoReceive(userInfo);
                    else
                        userInfoListener.onUserInfoFailure(new UserInfoException());
                }

        }
    }

    public void requestAccessCode(final Context ctx, String userName, String password) throws UnsupportedEncodingException
    {
        if (!NetworkUtil.isNetworkAvailable(ctx))
        {
            Log.e("requestAccessCode", "no internet : " + accessCodeListener.size());

            for (AccessCodeListener al : accessCodeListener)
                al.onAccessCodeFailure(new NoInternetException());

            return;
        }
        
        final String postParams = "client_id=" + Crypto.API_KEY +
                "&username=" + URLEncoder.encode(userName, "UTF-8") +
                "&password=" +password;

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                ServiceHandler sh = new ServiceHandler();
                String response = sh.makePostRequest(ctx,
                        AddressHandler.getUriAccessCode(),
                        postParams);

                Message msg = mHandler.obtainMessage(MSG_ACCESS_CODE_RECEIVED);

                try
                {
                    String parsedJson = parser.parseAccessCodeJson(response);
                    msg.arg1 = 1;
                    msg.obj = parsedJson;
                } catch (InvalidUserNamePasswordException e)
                {
                    e.printStackTrace();
                    msg.arg1 = -1;
                    msg.obj = e;
                }

                mHandler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * get info of this user
     * @param ctx
     * @param accessCode
     */
    public void requestUserInfo(final Context ctx, String accessCode)
    {
        if (!NetworkUtil.isNetworkAvailable(ctx))
        {
            if (userInfoListener != null)
                userInfoListener.onUserInfoFailure(new NoInternetException());

            return;
        }

        final String postParams = "client_id=" + Crypto.API_KEY +
                "&access_code=" + accessCode;

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                ServiceHandler sh = new ServiceHandler();
                String response = sh.makePostRequest(ctx,
                        AddressHandler.getUriUserInfo(),
                        postParams);
                UserInfo userInfo = parser.parseUserInfoJson(response);
                Message msg = mHandler.obtainMessage(MSG_USER_INFO_RECEIVED);
                msg.obj = userInfo;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    public void addAccessCodeListener(AccessCodeListener toAdd)
    {
        accessCodeListener.add(toAdd);
    }

    public void removeAccessCodeListener(AccessCodeListener toRemove)
    {
        accessCodeListener.remove(toRemove);
    }

    public void setUserInfoListener(UserInfoListener listener)
    {
        userInfoListener = listener;
    }

    public void removeUserInfoListener()
    {
        userInfoListener = null;
    }
}
