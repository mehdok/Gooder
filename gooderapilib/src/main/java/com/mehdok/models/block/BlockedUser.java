/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.block;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mehdok.models.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehdok on 5/2/2016.
 */
public class BlockedUser extends BaseResponse{
    @SerializedName("msg_data")
    @Expose
    public List<BlockedUserData> msgData = new ArrayList<BlockedUserData>();

    public List<BlockedUserData> getMsgData() {
        return msgData;
    }
}
