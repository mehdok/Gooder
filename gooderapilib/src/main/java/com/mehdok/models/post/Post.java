/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.models.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdok on 5/1/2016.
 */
public class Post {
    @SerializedName("pid") @Expose private String pid;
    @SerializedName("author") @Expose private Author author;
    @SerializedName("time") @Expose private String time;
    @SerializedName("parent_pid") @Expose private String parentPid;
    @SerializedName("title") @Expose private String title;
    @SerializedName("post_body") @Expose private String postBody;
    @SerializedName("comments_count") @Expose private String commentCount;
    @SerializedName("shares_count") @Expose private String sharesCount;
    @SerializedName("likes_count") @Expose private String likeCounts;
    @SerializedName("flags") @Expose private Flag flags;
    @SerializedName("extra") @Expose private Extra extra;

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

    public Extra getExtra() {
        return extra;
    }
}
