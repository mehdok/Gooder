/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.recommended;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mehdok.gooder.AndroidApplication;
import com.mehdok.gooder.R;
import com.mehdok.gooder.ui.recommended.fragments.RecommendedItemsFragment;
import com.mehdok.gooder.ui.recommended.navigation.RecommendedActivityDelegate;
import com.mehdok.gooder.utils.Util;
import com.mehdok.gooderapilib.QueryBuilder;

import retrofit2.adapter.rxjava.HttpException;

public class RecommendedActivity extends AppCompatActivity {

    private MenuItem mostLiked;
    private MenuItem mostReshare;
    private MenuItem mostCommented;
    private QueryBuilder.Type mSelectedType = QueryBuilder.Type.LIKES;
    private RecommendedItemsFragment recommendedItemsFragment;
    private CoordinatorLayout mRootLayout;

    public QueryBuilder.Type getmSelectedType() {
        return mSelectedType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecommendedActivityDelegate.subscribeOn(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended);
        Toolbar toolbar = (Toolbar) findViewById(R.id.recommended_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRootLayout = (CoordinatorLayout) findViewById(R.id.recommended_root_layout);

        recommendedItemsFragment = new RecommendedItemsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.recommended_base_fragment, recommendedItemsFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recommended, menu);

        mostLiked = menu.findItem(R.id.action_recommended_likes);
        mostReshare = menu.findItem(R.id.action_recommended_reshare);
        mostCommented = menu.findItem(R.id.action_recommented_comment);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_recommended_likes) {
            mostLiked.setChecked(!mostLiked.isChecked());
            mSelectedType = QueryBuilder.Type.LIKES;
        } else if (id == R.id.action_recommended_reshare) {
            mostReshare.setChecked(!mostReshare.isChecked());
            mSelectedType = QueryBuilder.Type.SHARES;
        } else if (id == R.id.action_recommented_comment) {
            mostCommented.setChecked(!mostCommented.isChecked());
            mSelectedType = QueryBuilder.Type.COMMENTS;
        }

        recommendedItemsFragment.refreshData();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RecommendedActivityDelegate.unSubscribe(this);
    }

    public void showBugSnackBar(Throwable e) {
        e.printStackTrace();
        String error;
        if (e instanceof HttpException) {
            error = ((HttpException) e).response().body().toString();
        } else {
            error = e.getMessage();
        }
        Snackbar.make(mRootLayout, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.send_report, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.sendBugReport(RecommendedActivity.this,
                                getString(R.string.bug_email_subject),
                                getString(R.string.bug_email_context));
                    }
                }).show();
    }

    public void showSimpleMessage(String str) {
        Snackbar.make(mRootLayout, str, Snackbar.LENGTH_SHORT)
                .show();
    }

    public FirebaseAnalytics getFirebaseAnalytics() {
        return ((AndroidApplication) getApplication()).getFirebaseAnalytics();
    }

}
