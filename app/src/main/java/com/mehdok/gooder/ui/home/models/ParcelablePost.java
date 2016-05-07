/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mehdok.gooderapilib.models.post.Post;

/**
 * Created by mehdok on 5/5/2016.
 */
public class ParcelablePost implements Parcelable {
    private String pid;
    private ParcelableAuthor author;
    private String time;
    private String parentPid;
    private String title;
    private String postBody;
    private String commentCount;
    private String sharesCount;
    private String likeCounts;
    private ParcelableFlag flags;
    private ParcelableExtra extra;
    private boolean liked;
    private boolean stared;

    public ParcelablePost(Post post) {
        pid = post.getPid();
        author = new ParcelableAuthor(post.getAuthor());
        time = post.getTime();
        parentPid = post.getParentPid();
        title = post.getTitle();
        postBody = post.getPostBody();
        commentCount = post.getCommentCount();
        sharesCount = post.getSharesCount();
        likeCounts = post.getLikeCounts();
        flags = new ParcelableFlag(post.getFlags());
        extra = new ParcelableExtra(post.getExtra());
        liked = post.isLiked();
        stared = post.isStarred();
    }

    public String getPid() {
        return pid;
    }

    public ParcelableAuthor getAuthor() {
        return author;
    }

    public String getTime() {
        return time;
    }

    public String getParentPid() {
        return parentPid;
    }

    public String getTitle() {
        return title;
    }

    public String getPostBody() {
        return postBody;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public String getSharesCount() {
        return sharesCount;
    }

    public String getLikeCounts() {
        return likeCounts;
    }

    public ParcelableFlag getFlags() {
        return flags;
    }

    public ParcelableExtra getExtra() {
        return extra;
    }

    public boolean isLiked() {
        return liked;
    }

    public boolean isStared() {
        return stared;
    }

    public void setStared(boolean stared) {
        this.stared = stared;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public void setSharesCount(String sharesCount) {
        this.sharesCount = sharesCount;
    }

    public void setLikeCounts(String likeCounts) {
        this.likeCounts = likeCounts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pid);
        parcel.writeParcelable(author, i);
        parcel.writeString(time);
        parcel.writeString(parentPid);
        parcel.writeString(title);
        parcel.writeString(postBody);
        parcel.writeString(commentCount);
        parcel.writeString(sharesCount);
        parcel.writeString(likeCounts);
        parcel.writeParcelable(flags, i);
        parcel.writeParcelable(extra, i);
        parcel.writeByte((byte) (liked ? 1 : 0));  //if myBoolean == true, byte == 1
        parcel.writeByte((byte) (stared ? 1 : 0));
    }

    private ParcelablePost(Parcel in) {
        pid = in.readString();
        author = in.readParcelable(ParcelableAuthor.class.getClassLoader());
        time = in.readString();
        parentPid = in.readString();
        title = in.readString();
        postBody = in.readString();
        commentCount = in.readString();
        sharesCount = in.readString();
        likeCounts = in.readString();
        flags = in.readParcelable(ParcelableFlag.class.getClassLoader());
        extra = in.readParcelable(ParcelableExtra.class.getClassLoader());
        liked = in.readByte() != 0;  //myBoolean == true if byte != 0
        stared = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ParcelablePost> CREATOR =
            new Parcelable.Creator<ParcelablePost>() {
                public ParcelablePost createFromParcel(Parcel in) {
                    return new ParcelablePost(in);
                }

                public ParcelablePost[] newArray(int size) {
                    return new ParcelablePost[size];
                }
            };
}
