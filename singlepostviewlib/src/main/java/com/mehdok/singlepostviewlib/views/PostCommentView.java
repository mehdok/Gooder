/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.utils.PrettySpann;
import com.mehdok.singlepostviewlib.utils.TimeUtil;
import com.mehdok.singlepostviewlib.utils.httpimage.URLImageParser;

/**
 * Created by mehdok on 5/4/2016.
 */
public class PostCommentView extends RelativeLayout {

    private PostTextView tvAuthor;
    private PostTextView tvDate;
    private PostTextView tvBody;
    private ImageView imgCommenterPhoto;

    public PostCommentView(Context context) {
        super(context);
        init(context);
    }

    public PostCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostCommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.post_comment, this);

        tvAuthor = (PostTextView) findViewById(R.id.comment_author);
        tvDate = (PostTextView) findViewById(R.id.comment_date);
        tvBody = (PostTextView) findViewById(R.id.comment_body);
        imgCommenterPhoto = (ImageView) findViewById(R.id.commenter_photo);
    }

    //    @Override
    //    protected void onFinishInflate() {
    //        Log.e("PostCommentView", "onFinishInflate");
    //        super.onFinishInflate();
    //
    //        tvAuthor = (PostTextView) findViewById(R.id.comment_author);
    //        tvDate = (PostTextView) findViewById(R.id.comment_date);
    //        tvBody = (PostTextView) findViewById(R.id.comment_body);
    //    }

    public void setComment(String author, String date, String body, String authorPhoto) {
        //TODO load user photo
        tvAuthor.setText(author);
        tvDate.setText(TimeUtil.getInstance().getReadableDate(date));
        tvBody.setPrettyText(PrettySpann.getPrettyString(body, null, new URLImageParser(tvBody)));

        Glide
                .with(getContext())
                .load(authorPhoto)
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(imgCommenterPhoto) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imgCommenterPhoto.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
}
