/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.utils;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mehdok on 4/29/2016.
 */
public class PrettySpann {
    public enum TagType {
        TAG(0),
        USER(1),
        POST(2);

        private int mValue;

        private TagType(int value) {
            mValue = value;
        }

        @Override
        public String toString() {
            return String.valueOf(mValue);
        }
    }

    public static final String HASH_TAG = "#!tag/";
    public static final String USER_TAG = "#!user/";
    public static final String POST_TAG = "#!post/";

    public static SpannableString getPrettyString(String str, TagClickListener clickListener,
                                                  Html.ImageGetter imageGetter)
    {
        if (str == null) return new SpannableString("");

        str = getCleanString(str);
        str = replaceForumUrl(str);
        str = replaceForumImage(str);
        Spanned spanned = linkifyHtml(str, imageGetter);
        return linkifyTags(spanned, clickListener);
    }

    public static String getCleanString(String str) {
        str = str.replace("\\r\\n", "<br\\>");
        str = str.replace("\\n", "<br\\>");
        return str;
    }

    private static String replaceForumUrl(String str) {
        int openTagIndex;
        while ((openTagIndex = str.indexOf("[url=")) != -1) {
            str = str.subSequence(0, openTagIndex) +
                    "<a href=\"" +
                    str.substring(openTagIndex + 5, str.length());
            int secondOpenTagIndex = str.indexOf("]", openTagIndex);
            str = str.subSequence(0, secondOpenTagIndex) +
                    "\">" +
                    str.substring(secondOpenTagIndex + 1, str.length());
            str = str.replace("[/url]", "</a>");
        }

        return str;
    }

    private static String replaceForumImage(String str) {
        str = str.replace("[img]", "<img src=\"");
        str = str.replace("[/img]", "\" alt=\"image\" align=\"middle\">");

        return str;
    }

    private static Spanned linkifyHtml(String cs, Html.ImageGetter imageGetter/* add image handler and so on*/) {
        if (imageGetter != null)
            return Html.fromHtml(cs, imageGetter, null);
        else
            return Html.fromHtml(cs);
    }

    private static String replaceForumTag(String str, String tagScheme) {
        return str.replace(tagScheme, "#");
    }

    private static SpannableString linkifyTags(CharSequence cs, TagClickListener clickListener) {
        SpannableString spannableString = new SpannableString(cs);

        Matcher matcher = Pattern.compile(HASH_TAG + "([ا-یA-Za-z0-9_-]+)").matcher(cs);
        while (matcher.find()) {
            spannableString.setSpan(new ClickableString(clickListener,
                            cs.subSequence(matcher.start() + HASH_TAG.length(), matcher.end()),
                            TagType.TAG),
                    matcher.start(),
                    matcher.end(),
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        matcher = Pattern.compile(USER_TAG + "([ا-یA-Za-z0-9_-]+)").matcher(cs);
        while (matcher.find()) {
            spannableString.setSpan(new ClickableString(clickListener,
                            cs.subSequence(matcher.start() + USER_TAG.length(), matcher.end()),
                            TagType.USER),
                    matcher.start(),
                    matcher.end(),
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        matcher = Pattern.compile(POST_TAG + "([ا-یA-Za-z0-9_-]+)").matcher(cs);
        while (matcher.find()) {
            spannableString.setSpan(new ClickableString(clickListener,
                            cs.subSequence(matcher.start() + POST_TAG.length(), matcher.end()),
                            TagType.POST),
                    matcher.start(),
                    matcher.end(),
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannableString;
    }

    private static class ClickableString extends ClickableSpan {
        private TagClickListener mListener;
        private CharSequence tag;
        private TagType tagType;

        public ClickableString(TagClickListener listener, CharSequence tag, TagType tagType) {
            mListener = listener;
            this.tag = tag;
            this.tagType = tagType;
        }

        @Override
        public void onClick(View v) {
            mListener.onTagClick(tag, tagType);
        }
    }

    public static interface TagClickListener {
        void onTagClick(CharSequence tag, TagType tagType);
    }
}
