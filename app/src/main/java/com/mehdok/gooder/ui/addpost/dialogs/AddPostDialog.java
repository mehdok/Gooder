/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.addpost.dialogs;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mehdok.gooder.R;
import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.database.DatabaseHelper;
import com.mehdok.gooder.ui.addpost.TextProccessor;
import com.mehdok.gooder.views.VazirButton;
import com.mehdok.gooder.views.VazirEditText;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.post.AddPost;
import com.mehdok.gooderapilib.models.user.UserInfo;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mehdok on 5/8/2016.
 */
public class AddPostDialog extends DialogFragment implements View.OnClickListener {

    private VazirEditText etTitle;
    private VazirEditText etBody;
    private VazirButton btnBold;
    private VazirButton btnItalic;
    private VazirButton btnUnderline;
    private VazirButton btnS;
    private VazirButton btnIMG;
    private VazirButton btnURL;
    private VazirButton btnUSER;
    private VazirButton btnPOST;
    private VazirButton btnTAG;
    private VazirButton btnSend;
    private VazirButton btnCancel;
    private AppCompatCheckBox chbDisableComment;
    private AppCompatCheckBox chbDisableReshare;
    private AppCompatCheckBox chbDraft;

    private TextProccessor textProccessor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullWidthDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_add_post, container, false);

        textProccessor = new TextProccessor();

        etTitle = (VazirEditText) rootView.findViewById(R.id.add_post_title);
        etBody = (VazirEditText) rootView.findViewById(R.id.add_post_body);
        btnBold = (VazirButton) rootView.findViewById(R.id.add_post_bold);
        btnItalic = (VazirButton) rootView.findViewById(R.id.add_post_italic);
        btnUnderline = (VazirButton) rootView.findViewById(R.id.add_post_underline);
        btnUnderline.setPaintFlags(btnUnderline.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnS = (VazirButton) rootView.findViewById(R.id.add_post_s);
        btnIMG = (VazirButton) rootView.findViewById(R.id.add_post_img);
        btnURL = (VazirButton) rootView.findViewById(R.id.add_post_url);
        btnUSER = (VazirButton) rootView.findViewById(R.id.add_post_user);
        btnPOST = (VazirButton) rootView.findViewById(R.id.add_post_post);
        btnTAG = (VazirButton) rootView.findViewById(R.id.add_post_tag);
        btnSend = (VazirButton) rootView.findViewById(R.id.add_post_send);
        btnCancel = (VazirButton) rootView.findViewById(R.id.add_post_cancel);
        chbDisableComment =
                (AppCompatCheckBox) rootView.findViewById(R.id.add_post_disable_comment);
        chbDisableReshare =
                (AppCompatCheckBox) rootView.findViewById(R.id.add_post_disable_reshare);
        chbDraft = (AppCompatCheckBox) rootView.findViewById(R.id.add_post_draft);

        btnBold.setOnClickListener(this);
        btnItalic.setOnClickListener(this);
        btnUnderline.setOnClickListener(this);
        btnS.setOnClickListener(this);
        btnIMG.setOnClickListener(this);
        btnURL.setOnClickListener(this);
        btnUSER.setOnClickListener(this);
        btnPOST.setOnClickListener(this);
        btnTAG.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.add_post_bold) {
            textProccessor.addBold(etBody);
        } else if (id == R.id.add_post_italic) {
            textProccessor.addItalic(etBody);
        } else if (id == R.id.add_post_underline) {
            textProccessor.addUnderLine(etBody);
        } else if (id == R.id.add_post_s) {
            textProccessor.addS(etBody);
        } else if (id == R.id.add_post_img) {
            textProccessor.addImg(etBody);
        } else if (id == R.id.add_post_url) {
            textProccessor.addUrl(etBody);
        } else if (id == R.id.add_post_user) {
            textProccessor.addUserTag(etBody);
        } else if (id == R.id.add_post_post) {
            textProccessor.addPostTag(etBody);
        } else if (id == R.id.add_post_tag) {
            textProccessor.addHashTag(etBody);
        } else if (id == R.id.add_post_send) {
            sendPost();
        } else if (id == R.id.add_post_cancel) {
            cancelPost();
        }
    }

    private void sendPost() {
        //TODO show waiting
        UserInfo userInfo = DatabaseHelper.getInstance(getActivity()).getUserInfo();
        RequestBuilder requestBuilder = new RequestBuilder();
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(userInfo.getUsername());
        try {
            queryBuilder.setPassword(Crypto.getMD5BASE64(
                    new String(Crypto.decrypt(userInfo.getPassword(), getActivity()))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean error = false;

        if (etTitle.getText().toString().isEmpty()) {
            etTitle.setError(getActivity().getString(R.string.required_field));
            error = true;
        }

        if (etBody.getText().toString().isEmpty()) {
            etBody.setError(getActivity().getString(R.string.required_field));
            error = true;
        }

        if (error) return;

        queryBuilder.setPostTitle(etTitle.getText().toString());
        queryBuilder.setPostBody(etBody.getText().toString());

        if (chbDisableComment.isChecked()) {
            queryBuilder.setDisableComments(QueryBuilder.Value.YES);
        } else {
            queryBuilder.setDisableComments(QueryBuilder.Value.NO);
        }

        if (chbDisableReshare.isChecked()) {
            queryBuilder.setDisableReshares(QueryBuilder.Value.YES);
        } else {
            queryBuilder.setDisableReshares(QueryBuilder.Value.NO);
        }

        if (chbDraft.isChecked()) {
            queryBuilder.setDraft(QueryBuilder.Value.YES);
        } else {
            queryBuilder.setDraft(QueryBuilder.Value.NO);
        }

        requestBuilder.addPost(queryBuilder)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddPost>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String error;
                        if (e instanceof HttpException) {
                            error = ((HttpException) e).response().body().toString();
                        } else {
                            error = e.getMessage();
                        }
                        Toast.makeText(AddPostDialog.this.getActivity(), error, Toast.LENGTH_SHORT)
                                .show();
                        cancelPost();
                    }

                    @Override
                    public void onNext(AddPost addPost) {
                        Toast.makeText(AddPostDialog.this.getActivity(), addPost.getMsgText(),
                                Toast.LENGTH_SHORT)
                                .show();
                        cancelPost();
                    }
                });
    }

    private void cancelPost() {
        dismiss();
    }
}
