/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.interfaces.SendCommentClickListener;

/**
 * Created by mehdok on 5/4/2016.
 */
public class AddCommentView extends RelativeLayout implements View.OnClickListener, TextWatcher {
    private ImageButton btnSend;
    private PostEditText etComment;
    private SendCommentClickListener listener;

    public AddCommentView(Context context) {
        super(context);
        init(context);
    }

    public AddCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddCommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.post_add_comment, this);
    }

    public void setSendCommentListener(SendCommentClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnSend = (ImageButton) findViewById(R.id.comment_send);
        btnSend.setOnClickListener(this);

        etComment = (PostEditText) findViewById(R.id.comment_field);
        etComment.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.sendComment(etComment.getText().toString());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 0) {
            btnSend.setEnabled(true);
        } else {
            btnSend.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
