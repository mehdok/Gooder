/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mehdok.singlepostviewlib.R;

/**
 * @author mehdok on 5/20/2016.
 *
 * <p>This class used to have unified glide setting whenever it needed.
 * anywhere there is need of using glide this class must used.
 * DO NOT USE GLIDE DIRECTLY</p>
 */
public class GlideHelper {
    public static void loadProfileImage(final Context ctx, String url, final ImageView imageView,
                                        Drawable placeHolder) {
        Glide
                .with(ctx)
                .load(url)
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.user)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
}
