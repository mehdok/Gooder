/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.utils;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.style.StrikethroughSpan;

import org.xml.sax.XMLReader;

/**
 * @author mehdok on 5/27/2016.
 * <p>This class is responsible for handling tag in {@link android.widget.TextView}
 * BUT it is for normal tags, not gooder tags</p>
 */
public class PrettyTagHandler implements Html.TagHandler {

    public void handleTag(boolean opening, String tag, Editable output,
                          XMLReader xmlReader) {
        if (tag.equalsIgnoreCase("strike") || tag.equals("s")) {
            processStrike(opening, output);
        }
    }

    private void processStrike(boolean opening, Editable output) {
        int len = output.length();
        if (opening) {
            output.setSpan(new StrikethroughSpan(), len, len, Spannable.SPAN_MARK_MARK);
        } else {
            Object obj = getLast(output, StrikethroughSpan.class);
            int where = output.getSpanStart(obj);

            output.removeSpan(obj);

            if (where != len) {
                output.setSpan(new StrikethroughSpan(), where, len,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private Object getLast(Editable text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);

        if (objs.length == 0) {
            return null;
        } else {
            for (int i = objs.length; i > 0; i--) {
                if (text.getSpanFlags(objs[i - 1]) == Spannable.SPAN_MARK_MARK) {
                    return objs[i - 1];
                }
            }
            return null;
        }
    }
}
