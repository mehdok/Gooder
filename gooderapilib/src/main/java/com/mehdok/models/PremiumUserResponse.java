/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/2/2016.
 */
public class PremiumUserResponse extends BaseResponse {
    @SerializedName("msg_data")
    @Expose
    private Boolean premium;

    public Boolean getPremium() {
        return premium;
    }
}
