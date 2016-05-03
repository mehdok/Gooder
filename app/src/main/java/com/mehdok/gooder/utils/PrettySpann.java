/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.utils;

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
public class PrettySpann
{
    public static SpannableString getPrettyString(String str, String tagScheme, TagClickListener clickListener)
    {
        str = Util.getCleanString(str);
        str = replaceForumUrl(str);
        str = replaceForumTag(str, tagScheme);
        Spanned spanned = linkifyHtml(str);
        return linkifyTags(spanned, clickListener);
    }

    private static String replaceForumUrl(String str)
    {
        int openTagIndex;
        while ((openTagIndex = str.indexOf("[url=")) != -1)
        {
            str = str.subSequence(0, openTagIndex) + "<a href=\"" + str.substring(openTagIndex + 5, str.length());
            int secondOpenTagIndex = str.indexOf("]", openTagIndex);
            str = str.subSequence(0, secondOpenTagIndex) + "\">" + str.substring(secondOpenTagIndex +1, str.length());
            str = str.replace("[/url]", "</a>");
        }

        return str;
    }

    private static Spanned linkifyHtml(String cs/* add image handler and so on*/)
    {
        return Html.fromHtml(cs);
    }

    private static String replaceForumTag(String str, String tagScheme)
    {
        return str.replace(tagScheme, "#");
    }

    private static SpannableString linkifyTags(CharSequence cs, TagClickListener clickListener)
    {
        SpannableString spannableString = new SpannableString(cs);

        Matcher matcher = Pattern.compile("#([ا-یA-Za-z0-9_-]+)").matcher(cs);
        while (matcher.find())
        {
            spannableString.setSpan(new ClickableString(clickListener, cs.subSequence(matcher.start() + 1, matcher.end())), matcher.start(), matcher.end(),
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannableString;
    }

    private static class ClickableString extends ClickableSpan
    {
        private TagClickListener mListener;
        private CharSequence tag;

        public ClickableString(TagClickListener listener, CharSequence tag) {
            mListener = listener;
            this.tag = tag;
        }
        @Override
        public void onClick(View v) {
            mListener.onTagClick(tag);
        }
    }

    public static interface TagClickListener
    {
        void onTagClick(CharSequence tag);
    }
}
