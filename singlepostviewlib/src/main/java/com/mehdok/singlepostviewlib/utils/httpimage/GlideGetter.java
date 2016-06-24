/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.utils.httpimage;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.mehdok.singlepostviewlib.R;
import com.mehdok.singlepostviewlib.utils.TransformationUtil;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by mehdok on 5/7/2016.
 * <p>This class is responsible to load image inside of {@link TextView} with Glide library</p>
 * <p> * @see <a https://github.com/floating-cat/S1-Next/blob/master/app/src/main/java/cl/monsoon/s1next/widget/GlideImageGetter.java">Link</a>
 </p>
 */
public class GlideGetter
        implements Html.ImageGetter, View.OnAttachStateChangeListener, Drawable.Callback {
    private final Context mContext;

    private final TextView mTextView;

    /**
     * Weak {@link java.util.HashSet}.
     */
    private final Set<ViewTarget<TextView, GlideDrawable>> mViewTargetSet =
            Collections.newSetFromMap(
                    new WeakHashMap<ViewTarget<TextView, GlideDrawable>, Boolean>());

    public GlideGetter(Context context, TextView textView) {
        this.mContext = context;
        this.mTextView = textView;

        // save Drawable.Callback in TextView
        // and get back when finish fetching image
        // see https://github.com/goofyz/testGlide/pull/1 for more details
        mTextView.setTag(R.id.tag_drawable_callback, this);
        // add this listener in order to clean any pending images loading
        // and set drawable callback tag to null when detached from window
        mTextView.addOnAttachStateChangeListener(this);
    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
        mTextView.invalidate();
    }

    @Override
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long l) {
    }

    @Override
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
    }

    @Override
    public void onViewAttachedToWindow(View view) {
    }

    @Override
    public void onViewDetachedFromWindow(View v) {

        // cancels any pending images loading
        for (ViewTarget<TextView, GlideDrawable> viewTarget : mViewTargetSet) {
            Glide.clear(viewTarget);
        }
        mViewTargetSet.clear();
        v.removeOnAttachStateChangeListener(this);

        v.setTag(R.id.tag_drawable_callback, null);
    }


    @Override
    public Drawable getDrawable(String url) {

        GlideUrlDrawable urlDrawable = new GlideUrlDrawable();
        ImageGetterViewTarget imageGetterViewTarget = new ImageGetterViewTarget(mTextView,
                urlDrawable);
        Glide.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //                .listener(new RequestListener<String, GlideDrawable>()
                //                {
                //                    @Override
                //                    public boolean onException(Exception e, String s, Target<GlideDrawable> glideDrawableTarget, boolean b)
                //                    {
                //                        if (e != null)
                //                        {
                //                            e.printStackTrace();
                //                        }
                //                        return false;
                //                    }
                //
                //                    @Override
                //                    public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> glideDrawableTarget, boolean b, boolean b2)
                //                    {
                //                        return false;
                //                    }
                //                })
                .transform(new TransformationUtil.GlMaxTextureSizeBitmapTransformation(mContext))
                .into(imageGetterViewTarget);

        mViewTargetSet.add(imageGetterViewTarget);
        return urlDrawable;
    }

    private static final class ImageGetterViewTarget extends ViewTarget<TextView, GlideDrawable> {

        private final GlideUrlDrawable mDrawable;

        private Request mRequest;

        private ImageGetterViewTarget(TextView view, GlideUrlDrawable drawable) {

            super(view);

            this.mDrawable = drawable;
        }

        @Override
        public void onResourceReady(GlideDrawable resource,
                                    GlideAnimation<? super GlideDrawable> glideAnimation) {

            // resize this drawable's width & height to fit its container
            final int resWidth = resource.getIntrinsicWidth();
            final int resHeight = resource.getIntrinsicHeight();
            int width, height;
            TextView textView = getView();
            if (textView.getWidth() >= resWidth) {
                width = resWidth;
                height = resHeight;
            } else {
                width = textView.getWidth();
                height = (int) (resHeight / ((float) resWidth / width));
            }

            Rect rect = new Rect(0, 0, width, height);
            resource.setBounds(rect);
            mDrawable.setBounds(rect);
            mDrawable.setDrawable(resource);

            if (resource.isAnimated()) {
                Drawable.Callback callback = (Drawable.Callback) textView.getTag(
                        R.id.tag_drawable_callback);
                // note: not sure whether callback would be null sometimes
                // when this Drawable' host view is detached from View
                if (callback != null) {
                    // set callback to drawable in order to
                    // signal its container to be redrawn
                    // to show the animated GIF
                    mDrawable.setCallback(callback);
                    resource.setLoopCount(GlideDrawable.LOOP_FOREVER);
                    resource.start();
                }
            } else {
                textView.setTag(R.id.tag_drawable_callback, null);
            }

            // see http://stackoverflow.com/questions/7870312/android-imagegetter-images-overlapping-text#comment-22289166
            textView.setText(textView.getText());
        }

        /**
         * See https://github.com/bumptech/glide/issues/550#issuecomment-123693051
         *
         * @see com.bumptech.glide.GenericRequestBuilder#into(com.bumptech.glide.request.target.Target)
         */
        @Override
        public Request getRequest() {
            return mRequest;
        }

        @Override
        public void setRequest(Request request) {
            this.mRequest = request;
        }
    }
}
