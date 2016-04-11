/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network;

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

    public String parseAccessCodeJson(String json)
    {
        if (json != null)
        {
            try
            {
                JSONObject mainJsonObject = new JSONObject(json);
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
}
