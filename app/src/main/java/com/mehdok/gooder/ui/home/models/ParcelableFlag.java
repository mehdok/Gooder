/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mehdok.gooderapilib.models.post.Flag;

/**
 * Created by mehdok on 5/5/2016.
 */
public class ParcelableFlag implements Parcelable {
    private boolean commentsDisabled;
    private boolean resharesDisabled;
    private boolean draft;
    private boolean edited;

    public ParcelableFlag(Flag flag) {
        commentsDisabled = flag.isCommentsDisabled();
        resharesDisabled = flag.isResharesDisabled();
        draft = flag.isDraft();
        edited = flag.isEdited();
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

    private ParcelableFlag(Parcel in) {
        commentsDisabled = in.readByte() != 0;  //myBoolean == true if byte != 0
        resharesDisabled = in.readByte() != 0;  //myBoolean == true if byte != 0
        draft = in.readByte() != 0;  //myBoolean == true if byte != 0
        edited = in.readByte() != 0;  //myBoolean == true if byte != 0
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (commentsDisabled ? 1 : 0));  //if myBoolean == true, byte == 1
        parcel.writeByte((byte) (resharesDisabled ? 1 : 0));  //if myBoolean == true, byte == 1
        parcel.writeByte((byte) (draft ? 1 : 0));  //if myBoolean == true, byte == 1
        parcel.writeByte((byte) (edited ? 1 : 0));  //if myBoolean == true, byte == 1
    }

    public static final Parcelable.Creator<ParcelableFlag> CREATOR =
            new Parcelable.Creator<ParcelableFlag>() {
                public ParcelableFlag createFromParcel(Parcel in) {
                    return new ParcelableFlag(in);
                }

                public ParcelableFlag[] newArray(int size) {
                    return new ParcelableFlag[size];
                }
            };
}
