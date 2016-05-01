/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network.model;

import android.text.SpannableString;

/**
 * Created by mehdok on 4/13/2016.
 */
public class Post
{
    private String pid;
    private Author author;
    private String time;
    private String parentPid;
    private String title;
    private SpannableString postBody;
    private String commentsCount;
    private String sharesCount;
    private String likesCount;
    private Flags flags;
    private Extra extra;

    public Post(String pid, Author author, String time, String parentPid, String title, SpannableString postBody, String commentsCount, String sharesCount, String likesCount, Flags flags, Extra extra)
    {
        this.pid = pid;
        this.author = author;
        this.time = time;
        this.parentPid = parentPid;
        this.title = title;
        this.postBody = postBody;
        this.commentsCount = commentsCount;
        this.sharesCount = sharesCount;
        this.likesCount = likesCount;
        this.flags = flags;
        this.extra = extra;
    }

    public String getPid()
    {
        return pid;
    }

    public Author getAuthor()
    {
        return author;
    }

    public String getTime()
    {
        return time;
    }

    public String getParentPid()
    {
        return parentPid;
    }

    public String getTitle()
    {
        return title;
    }

    public SpannableString getPostBody()
    {
        return postBody;
    }

    public String getCommentsCount()
    {
        return commentsCount;
    }

    public String getSharesCount()
    {
        return sharesCount;
    }

    public String getLikesCount()
    {
        return likesCount;
    }

    public Flags getFlags()
    {
        return flags;
    }

    public Extra getExtra()
    {
        return extra;
    }
}
