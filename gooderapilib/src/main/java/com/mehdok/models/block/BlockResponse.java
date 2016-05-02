/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.block;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mehdok.models.BaseResponse;

/**
 * Created by mehdok on 5/2/2016.
 */
public class BlockResponse extends BaseResponse {
    @SerializedName("msg_data")
    @Expose
    private boolean blocked;

    public boolean isBlocked() {
        return blocked;
    }
}
