/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.models;

import android.text.Html;
import android.text.SpannableString;

import com.mehdok.gooder.utils.PrettySpann;
import com.mehdok.gooder.utils.TimeUtil;
import com.mehdok.gooderapilib.models.post.Author;
import com.mehdok.gooderapilib.models.post.Extra;
import com.mehdok.gooderapilib.models.post.Flag;
import com.mehdok.gooderapilib.models.post.Post;
import com.orhanobut.logger.Logger;

/**
 * Created by mehdok on 5/2/2016.
 */
public class PrettyPost implements PrettySpann.TagClickListener {
    private final String TAG = "#!tag/";

    private String pid;
    private Author author;
    private String time;
    private String parentPid;
    private String title;
    private SpannableString postBody;
    private String commentCount;
    private String sharesCount;
    private String likeCounts;
    private Flag flags;
    private Extra extra;
    private boolean liked;
    private boolean stared;

    public PrettyPost(Post post, Html.ImageGetter imageGetter)
    {
        this.pid = post.getPid();
        this.author = post.getAuthor();
        this.time = TimeUtil.getInstance().getReadableDate(post.getTime());
        this.parentPid = post.getParentPid();
        this.title = post.getTitle();
        this.postBody =
                PrettySpann.getPrettyString(post.getPostBody(), this, imageGetter);//TODO image handler
        this.commentCount = post.getCommentCount();
        this.sharesCount = post.getSharesCount();
        this.likeCounts = post.getLikeCounts();
        this.flags = post.getFlags();
        this.extra = post.getExtra();
    }

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

    public SpannableString getPostBody() {
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

    public void setLikeCounts(String likeCounts) {
        this.likeCounts = likeCounts;
    }

    public Flag getFlags() {
        return flags;
    }

    public Extra getExtra() {
        return extra;
    }

    public boolean isStared() {
        return stared;
    }

    public void setStared(boolean stared) {
        this.stared = stared;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    @Override
    public void onTagClick(CharSequence tag, PrettySpann.TagType tagType) {
        Logger.t("onTagClick").d(tag.toString());
    }
}
