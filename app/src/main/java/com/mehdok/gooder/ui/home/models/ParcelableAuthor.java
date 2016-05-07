/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mehdok.gooderapilib.models.post.Author;

/**
 * Created by mehdok on 5/5/2016.
 */
public class ParcelableAuthor implements Parcelable {
    private String uid;
    private String fullName;

    public ParcelableAuthor(Author author) {
        uid = author.getUid();
        fullName = author.getFullName();
    }

    public String getUid() {
        return uid;
    }

    public String getFullName() {
        return fullName;
    }

    private ParcelableAuthor(Parcel in) {
        uid = in.readString();
        fullName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(fullName);
    }

    public static final Parcelable.Creator<ParcelableAuthor> CREATOR =
            new Parcelable.Creator<ParcelableAuthor>() {
                public ParcelableAuthor createFromParcel(Parcel in) {
                    return new ParcelableAuthor(in);
                }

                public ParcelableAuthor[] newArray(int size) {
                    return new ParcelableAuthor[size];
                }
            };
}
