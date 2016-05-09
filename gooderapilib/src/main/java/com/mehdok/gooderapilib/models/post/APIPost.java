/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib.models.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public String getPid() {
        return pid;
    }

    public Author getAuthor() {
        return author;
    }

    public String getTime() {
        return time;
    }

    //    public APIPost(String pid, Author author, String time, String parentPid, String title, SpannableString postBody, String commentsCount, String sharesCount, String likesCount, Flags flags, Extra extra)
    //    {
    //        this.pid = pid;
    //        this.author = author;
    //        this.time = time;
    //        this.parentPid = parentPid;
    //        this.title = title;
    //        this.postBody = postBody;
    //        this.commentsCount = commentsCount;
    //        this.sharesCount = sharesCount;
    //        this.likesCount = likesCount;
    //        this.flags = flags;
    //        this.extra = extra;
    //    }

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
}