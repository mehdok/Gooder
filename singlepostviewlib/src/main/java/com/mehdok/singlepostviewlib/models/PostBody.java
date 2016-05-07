/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.models;

import com.mehdok.singlepostviewlib.utils.PrettySpann;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostBody {
    private String body;
    private String note;
    private PrettySpann.TagClickListener tagClickListener;

    public PostBody(String body, String note, PrettySpann.TagClickListener tagClickListener) {
        this.body = body;
        this.note = note;
        this.tagClickListener = tagClickListener;
    }

    public String getBody() {
        return body;
    }

    public String getNote() {
        return note;
    }

    public PrettySpann.TagClickListener getTagClickListener() {
        return tagClickListener;
    }
}
