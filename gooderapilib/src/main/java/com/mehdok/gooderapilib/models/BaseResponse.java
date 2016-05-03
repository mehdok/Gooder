/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/2/2016.
 */
public class BaseResponse {
    @SerializedName("msg_code")
    @Expose
    public Integer msgCode;
    @SerializedName("msg_type")
    @Expose
    public String msgType;
    @SerializedName("msg_text")
    @Expose
    public String msgText;

    public Integer getMsgCode() {
        return msgCode;
    }

    public String getMsgType() {
        return msgType;
    }

    public String getMsgText() {
        return msgText;
    }
}
