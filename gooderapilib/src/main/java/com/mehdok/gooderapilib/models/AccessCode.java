/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/1/2016.
 */
public class AccessCode extends BaseResponse {
    @SerializedName("msg_data")
    @Expose
    public AccessCodeData msgData;

    public AccessCodeData getMsgData() {
        return msgData;
    }
}
