/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.addpost;

import android.support.v7.widget.AppCompatEditText;

import com.mehdok.gooder.views.VazirEditText;

/**
 * Created by mehdok on 5/9/2016.
 */
public class TextProccessor {

    private final String BOLD_MARK = "*";
    private final String ITALIC_MARK = "^";
    private final String UNDERLINE_MARK = "_";
    private final String S_MARK = "-";
    private final String IMG_START = "[img]";
    private final String IMG_END = "[/img]";
    private final String URL_START = "[url=";
    private final String URL_END = "][/url]";
    private final String USER_TAG = "#!user/";
    private final String POST_TAG = "#!post/";
    private final String HASH_TAG = "#!tag/";

    public void addBold(VazirEditText editText) {
        int selectionBounds[] = getSelectionBounds(editText);
        if (selectionBounds[1] <= 0) {
            editText.setText(
                    String.format("%s%s%s", editText.getText().toString(), BOLD_MARK, BOLD_MARK));
        } else {
            addHyperMark(editText, selectionBounds, BOLD_MARK, BOLD_MARK);
        }
    }

    public void addItalic(VazirEditText editText) {
        int selectionBounds[] = getSelectionBounds(editText);
        if (selectionBounds[1] <= 0) {
            editText.setText(String.format("%s%s%s", editText.getText().toString(), ITALIC_MARK,
                    ITALIC_MARK));
        } else {
            addHyperMark(editText, selectionBounds, ITALIC_MARK, ITALIC_MARK);
        }
    }

    public void addUnderLine(VazirEditText editText) {
        int selectionBounds[] = getSelectionBounds(editText);
        if (selectionBounds[1] <= 0) {
            editText.setText(String.format("%s%s%s", editText.getText().toString(), UNDERLINE_MARK,
                    UNDERLINE_MARK));
        } else {
            addHyperMark(editText, selectionBounds, UNDERLINE_MARK, UNDERLINE_MARK);
        }
    }

    public void addS(VazirEditText editText) {
        int selectionBounds[] = getSelectionBounds(editText);
        if (selectionBounds[1] <= 0) {
            editText.setText(
                    String.format("%s%s%s", editText.getText().toString(), S_MARK, S_MARK));
        } else {
            addHyperMark(editText, selectionBounds, S_MARK, S_MARK);
        }
    }

    public void addImg(VazirEditText editText) {
        int selectionBounds[] = getSelectionBounds(editText);
        if (selectionBounds[1] <= 0) {
            editText.setText(
                    String.format("%s%s%s", editText.getText().toString(), IMG_START, IMG_END));
        } else {
            addHyperMark(editText, selectionBounds, IMG_START, IMG_END);
        }
    }

    public void addUrl(VazirEditText editText) {
        int selectionBounds[] = getSelectionBounds(editText);
        if (selectionBounds[1] <= 0) {
            editText.setText(
                    String.format("%s%s%s", editText.getText().toString(), URL_START, URL_END));
        } else {
            addHyperMark(editText, selectionBounds, URL_START, URL_END);
        }
    }

    public void addUserTag(VazirEditText editText) {
        int selectionBounds[] = getSelectionBounds(editText);
        if (selectionBounds[1] <= 0) {
            editText.setText(
                    String.format("%s%s", editText.getText().toString(), USER_TAG));
        } else {
            addHyperMark(editText, selectionBounds, USER_TAG, "");
        }
    }

    public void addPostTag(VazirEditText editText) {
        int selectionBounds[] = getSelectionBounds(editText);
        if (selectionBounds[1] <= 0) {
            editText.setText(
                    String.format("%s%s", editText.getText().toString(), POST_TAG));
        } else {
            addHyperMark(editText, selectionBounds, POST_TAG, "");
        }
    }

    public void addHashTag(VazirEditText editText) {
        int selectionBounds[] = getSelectionBounds(editText);
        if (selectionBounds[1] <= 0) {
            editText.setText(
                    String.format("%s%s", editText.getText().toString(), HASH_TAG));
        } else {
            addHyperMark(editText, selectionBounds, HASH_TAG, "");
        }
    }

    private int[] getSelectionBounds(AppCompatEditText editText) {
        int[] result = new int[2];
        result[0] = editText.getSelectionStart();
        result[1] = editText.getSelectionEnd();

        return result;
    }

    private void addHyperMark(AppCompatEditText editText, int[] bounds, String startMark,
                              String endMark) {
        String originalText = editText.getText().toString();
        String bolded =
                startMark +
                        originalText.substring(bounds[0], bounds[1]) +
                        endMark;
        bolded = originalText.substring(0, bounds[0]) +
                bolded +
                originalText.substring(bounds[1], originalText.length());
        editText.setText(bolded);
    }
}
