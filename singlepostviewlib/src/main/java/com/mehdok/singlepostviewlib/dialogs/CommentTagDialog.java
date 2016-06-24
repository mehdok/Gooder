/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.utils.ClipBoardUtil;
import com.mehdok.singlepostviewlib.views.PostEditText;

/**
 * @author mehdok on 6/4/2016.
 * <p>This class extends a {@link DialogFragment} to make mentioning easier.
 * User can select mention type and press paste button.</p>
 *
 */
public class CommentTagDialog extends DialogFragment {
    public static final String ET_HINT = "hint";
    public static final String DIALOG_TITLE = "title";
    private final String USER_TAG = "#!user/";
    private final String POST_TAG = "#!post/";
    private final String HASH_TAG = "#!tag/";

    private int titleResource;
    private int hintResource;

    private OnOkClickedListener listener;

    public interface OnOkClickedListener {
        void OnOkCLicked(String text);
    }

    public CommentTagDialog setOnOkClickListener(OnOkClickedListener listener) {
        this.listener = listener;
        return this;
    }

    public CommentTagDialog() {
        // Required empty public constructor
    }

    public static CommentTagDialog newInstance(int titleRes, int hintRes) {
        CommentTagDialog fragment = new CommentTagDialog();
        Bundle args = new Bundle();
        args.putInt(ET_HINT, hintRes);
        args.putInt(DIALOG_TITLE, titleRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CommentTagDialogStyle);
        if (getArguments() != null) {
            titleResource = getArguments().getInt(DIALOG_TITLE, R.string.comment_tag_title);
            hintResource = getArguments().getInt(ET_HINT, R.string.comment_tag_hint);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View content = inflateLayout();

        final RadioGroup radioGroup = (RadioGroup) content.findViewById(R.id.comment_tag_type);
        final PostEditText editText = (PostEditText) content.findViewById(R.id.dialog_comment_et_1);
        editText.setHint(hintResource);
        AppCompatImageButton paste =
                (AppCompatImageButton) content.findViewById(R.id.dialog_comment_et_1_paste);
        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText(ClipBoardUtil.pasteText(getContext()));
            }
        });

        return new AlertDialog.Builder(getContext())
                .setTitle(titleResource)
                .setView(content)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            String input = processInput(radioGroup.getCheckedRadioButtonId(),
                                    editText.getText().toString());
                            listener.OnOkCLicked(input);
                        }
                        dismiss();
                    }
                })
                .create();
    }

    protected View inflateLayout() {
        LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.dialog_comment_tag, ((ViewGroup) getView()), false);
    }

    private String processInput(int selection, String input) {
        if (selection == R.id.comment_tag_user) {
            return USER_TAG + input;
        } else if (selection == R.id.comment_tag_post) {
            return POST_TAG + input;
        } else if (selection == R.id.comment_tag_hash) {
            return HASH_TAG + input;
        }

        return "";
    }
}
