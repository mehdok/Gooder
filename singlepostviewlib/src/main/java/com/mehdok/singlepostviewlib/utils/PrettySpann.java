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

    /**
     * This method clean the input, replace the specific forum tag, handle http link and tag and
     * showing image, the result is cleaned Spannable that handle all of mentioned specific
     *
     * @param str           the input
     * @param clickListener this is fo later you, NOW the tag handle manually
     * @param imageGetter   Html image getter with glide handler to load images in TextView
     * @return the cleaned text
     */
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

    /**
     * Replace the \r\n with html tag
     *
     * @param str the input
     * @return
     */
    public static String getCleanString(String str) {
        str = str.replace("\\r\\n", "<br\\>");
        str = str.replace("\\n", "<br\\>");
        return str;
    }

    /**
     * Replace [url=http://google.com][/url] with <a href="http://google.com"></a>
     *
     * @param str the input
     * @return
     */
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

    /**
     * Replace [img]http://google.com/image.jpg[/img] With
     * <img src="http://google.com/image.jpg" alt="image" align="middle">
     *
     * @param str the input
     * @return
     */
    private static String replaceForumImage(String str) {
        str = str.replace("[img]", "<img src=\"");
        str = str.replace("[/img]", "\" alt=\"image\" align=\"middle\">");

        return str;
    }

    /**
     * Use Html.fromHtml to linkify url and handle image loading process
     *
     * @param cs the input String
     * @param imageGetter image getter instance
     * @return
     */
    private static Spanned linkifyHtml(String cs, Html.ImageGetter imageGetter/* add image handler and so on*/) {
        if (imageGetter != null)
            return Html.fromHtml(cs, imageGetter, null);
        else
            return Html.fromHtml(cs);
    }

    private static String replaceForumTag(String str, String tagScheme) {
        return str.replace(tagScheme, "#");
    }

    /**
     * Because There is several tag (user, post, real tag) we need to handle all of them manually
     * This method find all of 3 mentioned tag and set a click listener for it.
     *
     * @param cs the input CharSequence, this may be an Spannable
     * @param clickListener clickListener to handle click
     * @return
     */
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
