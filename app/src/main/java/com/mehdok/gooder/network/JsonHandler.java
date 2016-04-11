/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network;

import android.content.Context;
import android.os.Message;

import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.network.handler.JsonThreadHandler;
import com.mehdok.gooder.network.interfaces.AccessCodeListener;

/**
 * Created by mehdok on 4/11/2016.
 */
public class JsonHandler
{
    private static JsonHandler mInstance;
    private JsonThreadHandler mHandler;
    private JsonParser parser;

    private final int MSG_ACCESS_CODE_RECEIVED = 1;

    private AccessCodeListener accessCodeListener;

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
                String parsedJson = (String)msg.obj;
                if (!parsedJson.equals(""))
                {
                    if (accessCodeListener != null)
                        accessCodeListener.onAccessCodeReceive(parsedJson);
                }
                else
                {
                    if (accessCodeListener != null)
                        accessCodeListener.onAccessCodeFailure("something went wrong during getting access code");// TODO localize error
                }
                break;
        }
    }

    public void requestAccessCode(final Context ctx, String userName, String password)
    {
        if (!NetworkUtil.isNetworkAvailable(ctx))
        {
            if (accessCodeListener != null)
                accessCodeListener.onAccessCodeFailure("No active internet connection");

            return;
        }
        
        final String postParams = "client_id=" + Crypto.API_KEY +
                "&username=" + userName +
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
                String parsedJson = parser.parseAccessCodeJson(response);
                Message msg = mHandler.obtainMessage(MSG_ACCESS_CODE_RECEIVED);
                msg.obj = parsedJson;
                mHandler.sendMessage(msg);
            }
        });
    }

    public void setAccessCodeListener(AccessCodeListener accessCodeListener)
    {
        this.accessCodeListener = accessCodeListener;
    }

    public void removeAccessCodeListener()
    {
        accessCodeListener = null;
    }
}
