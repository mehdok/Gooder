/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.models.PostBody;
import com.mehdok.singlepostviewlib.utils.PrettySpann;
import com.mehdok.singlepostviewlib.utils.httpimage.GlideGetter;

/**
 * @author mehdok on 5/4/2016.
 * <p>A Compound View for showing body, it will use {@link PrettySpann} to get pretty string</p>
 */
public class PostBodyView extends LinearLayout {
    private PostTextView tvBody;

    public PostBodyView(Context context) {
        super(context);
        init(context);
    }

    public PostBodyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostBodyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.post_body, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvBody = (PostTextView) findViewById(R.id.body_text);
    }

    public void setPostBody(PostBody postBody) {
        tvBody.setVisibility(View.VISIBLE);
        SpannableString body = PrettySpann.getPrettyString(postBody.getBody(),
                postBody.getTagClickListener(), new GlideGetter(tvBody.getContext(), tvBody));
        tvBody.setPrettyText(body);
    }

    public void setClickListener(OnClickListener clickListener) {
        tvBody.setOnClickListener(clickListener);
    }
}
