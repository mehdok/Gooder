/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.addgroup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/2/2016.
 */
public class GroupData {
    @SerializedName("gid")
    @Expose
    public String gid;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("uid")
    @Expose
    public String uid;

    public String getGid() {
        return gid;
    }

    /**
     * nullable
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * nullable
     *
     * @return
     */
    public String getUid() {
        return uid;
    }
}
