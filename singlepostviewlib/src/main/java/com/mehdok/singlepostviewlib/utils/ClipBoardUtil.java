/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.utils;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * @author mehdok  on 5/21/2016.
 * <p>This class is responsible for copy/past texts into/from clipboard</p>
 */
public class ClipBoardUtil {
    private static final String TAG = "Gooder";

    /**
     * copy some text into the clipboard
     *
     * @param ctx          {@link Context}
     * @param charSequence text to put in clipboard
     */
    public static void copyText(Context ctx, CharSequence charSequence) {
        android.content.ClipboardManager cm =
                (android.content.ClipboardManager) ctx.getSystemService(
                        Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(TAG, charSequence));
    }

    /**
     * get a {@link String} from clipboard and return it
     * @param ctx {@link Context}
     * @return first string in clicpboard
     */
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
