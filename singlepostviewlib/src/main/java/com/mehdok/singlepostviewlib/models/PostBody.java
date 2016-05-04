/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.models;

import android.text.SpannableString;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostBody {
    private SpannableString body;
    private SpannableString note;

    public PostBody(SpannableString body, SpannableString note) {
        this.body = body;
        this.note = note;
    }

    public SpannableString getBody() {
        return body;
    }

    public SpannableString getNote() {
        return note;
    }
}
