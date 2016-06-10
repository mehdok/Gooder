/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.about;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mehdok.gooder.R;
import com.mehdok.gooder.utils.Util;

public class AboutActivity extends AppCompatActivity {

    private final int START_DELAY = 1000; //ms
    private final int TEXT_MOVE_IN_TIME = 500; //ms
    private final String ABOUT_TEXT =
            "Android client for <a href=\"http://www.gooder.in\">gooder.in</a> <br/><br/>If you don\'t have permium account" +
                    " just use <font color=\"#f8a31a\">naneveshte</font> as username and password" +
                    "<br/><br/><br/>&#x24B8; Mehdi Sohrabi 2016 ";

    RelativeLayout logoContainerLayout;
    RelativeLayout bothTextLayout;
    ImageView largeLogo;
    TextView gooderText;
    TextView versionText;
    TextView aboutGooderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        logoContainerLayout = (RelativeLayout) findViewById(R.id.logo_container);
        largeLogo = (ImageView) findViewById(R.id.gooder_image);
        gooderText = (TextView) findViewById(R.id.gooder_text);
        versionText = (TextView) findViewById(R.id.version_text);
        bothTextLayout = (RelativeLayout) findViewById(R.id.both_text_layout);
        aboutGooderText = (TextView) findViewById(R.id.about_gooder_text);

        versionText.setText(" v " + Util.getAppVersionName(this));
        aboutGooderText.setText(Html.fromHtml(ABOUT_TEXT), TextView.BufferType.SPANNABLE);
        aboutGooderText.setMovementMethod(LinkMovementMethod.getInstance());

        startAnimation();
    }

    private void startAnimation() {
        logoContainerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                showLogo();
            }
        }, START_DELAY);
    }

    public void showLogo() {
        Animation logoAppearAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_appear);
        logoAppearAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveTextsIn();
            }
        });

        largeLogo.startAnimation(logoAppearAnimation);
        largeLogo.setVisibility(View.VISIBLE);
    }

    public void moveTextsIn() {
        bothTextLayout.post(new Runnable() {
            @Override
            public void run() {
                int[] coor = new int[2];
                bothTextLayout.getLocationInWindow(coor);
                TranslateAnimation
                        trans =
                        new TranslateAnimation(0, 0, bothTextLayout.getHeight() + coor[1], 0);
                trans.setDuration(TEXT_MOVE_IN_TIME);
                trans.setFillAfter(true);
                trans.setInterpolator(new AccelerateDecelerateInterpolator());
                trans.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fadeInAboutText();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                bothTextLayout.startAnimation(trans);
                gooderText.setVisibility(View.VISIBLE);
                versionText.setVisibility(View.VISIBLE);
            }
        });
    }

    public void fadeInAboutText() {
        aboutGooderText.post(new Runnable() {
            @Override
            public void run() {
                Animation aboutSnailFadeInAnimation =
                        AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                aboutGooderText.startAnimation(aboutSnailFadeInAnimation);
                aboutGooderText.setVisibility(View.VISIBLE);
            }
        });
    }
}
