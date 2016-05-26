/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.utils;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by mehdok on 5/21/2016.
 */
public class ClipBoardUtil {
    private static final String TAG = "Gooder";

    public static void copyText(Context ctx, CharSequence charSequence) {
        android.content.ClipboardManager cm =
                (android.content.ClipboardManager) ctx.getSystemService(
                        Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(TAG, charSequence));
    }

    public static String pasteText(Context ctx) {
        ClipboardManager clipboard =
                (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            android.content.ClipDescription description = clipboard.getPrimaryClipDescription();
            android.content.ClipData data = clipboard.getPrimaryClip();
            if (data != null &&
                    description != null &&
                    description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                return String.valueOf(data.getItemAt(0).getText());
            }
        }

        return "";
    }
}
