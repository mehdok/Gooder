/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network.model;

/**
 * Created by mehdok on 4/13/2016.
 */
public class Flags {
    private boolean commentsDisabled;
    private boolean resharesDisabled;
    private boolean draft;
    private boolean edited;

    public Flags(boolean commentsDisabled, boolean resharesDisabled, boolean draft,
                 boolean edited) {
        this.commentsDisabled = commentsDisabled;
        this.resharesDisabled = resharesDisabled;
        this.draft = draft;
        this.edited = edited;
    }

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
