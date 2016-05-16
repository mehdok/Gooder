/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/1/2016.
 */
public class Extra {
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("url")
    @Expose
    private String url;

    public String getNote() {
        return note;
    }

    public String getUrl() {
        return url;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
