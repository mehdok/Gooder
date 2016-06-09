/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by mehdok on 5/1/2016.
 */
public class APIPost {
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("author")
    @Expose
    private Author author;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("parent_pid")
    @Expose
    private String parentPid;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("post_body")
    @Expose
    private String postBody;
    @SerializedName("comments_count")
    @Expose
    private String commentCount;
    @SerializedName("shares_count")
    @Expose
    private String sharesCount;
    @SerializedName("likes_count")
    @Expose
    private String likeCounts;
    @SerializedName("flags")
    @Expose
    private Flag flags;
    @SerializedName("starred")
    @Expose
    private boolean starred;
    @SerializedName("liked")
    @Expose
    private boolean liked;
    @SerializedName("extra")
    @Expose
    private Extra extra;

    private boolean read;
    private ArrayList<APIPost> reshareChains;

    public String getPid() {
        return pid;
    }

    public Author getAuthor() {
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

    public Flag getFlags() {
        return flags;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public Extra getExtra() {
        return extra;
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

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public ArrayList<APIPost> getReshareChains() {
        return reshareChains;
    }

    public void setReshareChains(
            ArrayList<APIPost> reshareChains) {
        this.reshareChains = reshareChains;
    }
}
