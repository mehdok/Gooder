/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mehdok.gooderapilib.models.post.Extra;

/**
 * Created by mehdok on 5/5/2016.
 */
public class ParcelableExtra implements Parcelable {
    private String note;
    private String url;

    public ParcelableExtra(Extra extra) {
        note = extra.getNote();
        url = extra.getUrl();
    }

    public String getNote() {
        return note;
    }

    public String getUrl() {
        return url;
    }

    private ParcelableExtra(Parcel in) {
        note = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(note);
        parcel.writeString(url);
    }

    public static final Parcelable.Creator<ParcelableExtra> CREATOR =
            new Parcelable.Creator<ParcelableExtra>() {
                public ParcelableExtra createFromParcel(Parcel in) {
                    return new ParcelableExtra(in);
                }

                public ParcelableExtra[] newArray(int size) {
                    return new ParcelableExtra[size];
                }
            };
}
