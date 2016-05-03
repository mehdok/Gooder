/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/2/2016.
 */
public class CommentInfo {
    @SerializedName("pid")
    @Expose
    public String pid;
    @SerializedName("cid")
    @Expose
    public String cid;

    public String getPid() {
        return pid;
    }

    public String getCid() {
        return cid;
    }
}
