/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/1/2016.
 */
public class Flag {
    @SerializedName("comments-disabled")
    @Expose
    private boolean commentsDisabled;
    @SerializedName("reshares-disabled")
    @Expose
    private boolean resharesDisabled;
    @SerializedName("draft")
    @Expose
    private boolean draft;
    @SerializedName("edited")
    @Expose
    private boolean edited;

    public boolean isCommentsDisabled() {
        return commentsDisabled;
    }

    public boolean isResharesDisabled() {
        return resharesDisabled;
    }

    public boolean isDraft() {
        return draft;
    }

    public boolean isEdited() {
        return edited;
    }
}
