/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.addpost.dialogs;

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

import com.mehdok.gooder.R;
import com.mehdok.singlepostviewlib.utils.ClipBoardUtil;
import com.mehdok.singlepostviewlib.views.PostEditText;

/**
 * Created by mehdok on 5/26/2016.
 */
public class SingleEditTextDialog extends DialogFragment {

    public static final String ET_HINT = "hint";
    public static final String DIALOG_TITLE = "title";

    private PostEditText editText;
    private AppCompatImageButton paste;

    private int titleResource;
    private int hintResource;

    private OnOkClickedListener listener;

    public SingleEditTextDialog setOnOkClickListener(OnOkClickedListener listener) {
        this.listener = listener;
        return this;
    }

    public SingleEditTextDialog() {
        // Required empty public constructor
    }

    public static SingleEditTextDialog newInstance(int titleRes, int hintRes) {
        SingleEditTextDialog fragment = new SingleEditTextDialog();
        Bundle args = new Bundle();
        args.putInt(ET_HINT, hintRes);
        args.putInt(DIALOG_TITLE, titleRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SingleEditTextDialogStyle);
        if (getArguments() != null) {
            titleResource = getArguments().getInt(DIALOG_TITLE, R.string.add_img_dialog_title);
            hintResource = getArguments().getInt(ET_HINT, R.string.add_img_et_hint);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View content = inflateLayout();

        final PostEditText editText = (PostEditText) content.findViewById(R.id.dialog_single_et_1);
        editText.setHint(hintResource);
        AppCompatImageButton paste =
                (AppCompatImageButton) content.findViewById(R.id.dialog_single_et_1_paste);
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
                            listener.OnOkCLicked(editText.getText().toString());
                        }
                        dismiss();
                    }
                })
                .create();
    }

    protected View inflateLayout() {
        LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.dialog_single_et, ((ViewGroup) getView()), false);
    }

    public interface OnOkClickedListener {
        void OnOkCLicked(String text);
    }
}
