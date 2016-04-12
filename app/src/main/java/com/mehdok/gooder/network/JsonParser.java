/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network;

import com.mehdok.gooder.network.exceptions.InvalidUserNamePasswordException;
import com.mehdok.gooder.network.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mehdok on 4/11/2016.
 */
public class JsonParser
{
    private final String MSG_CODE = "msg_code";
    private final String MSG_TYPE = "msg_type";
    private final String MSG_TEXT = "msg_text";
    private final String MSG_DATA = "msg_data";
    private final String ACCESS_CODE = "access_code";
    private final String USER_ID = "uid";
    private final String USERNAME = "username";
    private final String FULLNAME = "fullname";
    private final String AVATAR = "avatar";
    private final String ABOUT = "about";
    private final String WEB = "web";

    public String parseAccessCodeJson(String json) throws InvalidUserNamePasswordException
    {
        if (json != null)
        {
            try
            {
                JSONObject mainJsonObject = new JSONObject(json);

                // if user and password did not match throw an exception
                String msgText = mainJsonObject.getString(MSG_TEXT);
                if (msgText.equalsIgnoreCase("Invalid username or password."))
                    throw new InvalidUserNamePasswordException();

                JSONObject dataObject = mainJsonObject.getJSONObject(MSG_DATA);
                String accessCode = dataObject.getString(ACCESS_CODE);

                return accessCode;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return "";
    }

    public UserInfo parseUserInfoJson(String json)
    {
        if (json != null)
        {
            try
            {
                JSONObject mainJsonObject = new JSONObject(json);
                UserInfo userInfo = new UserInfo(mainJsonObject.getString(USER_ID),
                        mainJsonObject.getString(USERNAME),
                        mainJsonObject.getString(FULLNAME),
                        mainJsonObject.getString(AVATAR),
                        mainJsonObject.getString(ABOUT),
                        mainJsonObject.getString(WEB),
                        null);

                return userInfo;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }
}
