/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.singlepost;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mehdok.gooder.R;
import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.database.DatabaseHelper;
import com.mehdok.gooder.ui.home.models.ParcelablePost;
import com.mehdok.gooder.utils.Util;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.comment.CommentContent;
import com.mehdok.gooderapilib.models.comment.CommentResponse;
import com.mehdok.gooderapilib.models.user.UserInfo;
import com.mehdok.singlepostviewlib.interfaces.FunctionButtonClickListener;
import com.mehdok.singlepostviewlib.interfaces.SendCommentClickListener;
import com.mehdok.singlepostviewlib.models.Post;
import com.mehdok.singlepostviewlib.models.PostBody;
import com.mehdok.singlepostviewlib.models.PostComment;
import com.mehdok.singlepostviewlib.models.PostDetail;
import com.mehdok.singlepostviewlib.models.PostFunction;
import com.mehdok.singlepostviewlib.utils.PrettySpann;
import com.mehdok.singlepostviewlib.views.SinglePostView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SinglePostActivity extends AppCompatActivity implements FunctionButtonClickListener,
        SendCommentClickListener, PrettySpann.TagClickListener {

    public static final String PARCELABLE_POST_EXTRA = "parcelable_post_extra";

    private SinglePostView singlePostView;
    private CoordinatorLayout mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        singlePostView = (SinglePostView) findViewById(R.id.single_post_view);
        mRootLayout = (CoordinatorLayout) findViewById(R.id.single_post_root_layout);

        setUpToolbar();
        ParcelablePost post = getExtra();
        showPost(post);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.single_post_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private ParcelablePost getExtra() {
        return getIntent().getParcelableExtra(PARCELABLE_POST_EXTRA);
    }

    private void showPost(ParcelablePost post) {
        PostDetail postDetail = new PostDetail(post.getAuthor().getFullName(), post.getTime());
        PostBody postBody = new PostBody(post.getPostBody(), post.getExtra().getNote(), this);
        PostFunction postFunction = new PostFunction(Integer.valueOf(post.getLikeCounts()),
                Integer.valueOf(post.getSharesCount()),
                Integer.valueOf(post.getCommentCount()),
                this);

        Post poster = new Post(postDetail, postBody, postFunction, this);

        singlePostView.showPost(poster);

        UserInfo userInfo = DatabaseHelper.getInstance(this).getUserInfo();
        RequestBuilder requestBuilder = new RequestBuilder();
        String pass = "";
        try {
            pass = (Crypto.getMD5BASE64(
                    new String(Crypto.decrypt(userInfo.getPassword(), this))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        requestUserInfo(post.getAuthor().getUid(), userInfo.getUsername(), pass, requestBuilder);
        requestComment(post.getPid(), userInfo.getUsername(), pass, requestBuilder);
    }

    @Override
    public void likeClicked() {
        //TODO
    }

    @Override
    public void shareClicked() {
        //TODO
    }

    @Override
    public void starClicked() {
        //TODO
    }

    @Override
    public void sendComment(String comment) {
        //TODO
    }

    private void requestUserInfo(String uid, String userName, String password,
                                 RequestBuilder requestBuilder) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(userName);
        queryBuilder.setPassword(password);
        queryBuilder.setUid(uid);
        requestBuilder.getUserInfo(queryBuilder)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showBugSnackBar(e);
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        singlePostView.addUserPhoto(userInfo.getAvatar());
                    }
                });
    }

    private void requestComment(String pid, String userName, String password,
                                RequestBuilder requestBuilder) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(userName);
        queryBuilder.setPassword(password);
        requestBuilder.getPostComments(pid, queryBuilder)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<CommentResponse, Observable<ArrayList<PostComment>>>() {
                    @Override
                    public Observable<ArrayList<PostComment>> call(
                            CommentResponse commentResponse) {
                        ArrayList<PostComment> postComments =
                                new ArrayList<PostComment>(commentResponse.getCommentList().size());
                        for (CommentContent response : commentResponse.getCommentList()) {
                            PostComment post = new PostComment(response.getCommentAuthor().fullname,
                                    response.getTime(), response.getContent(),
                                    response.getCommentAuthor().getAvatar());
                            postComments.add(post);
                        }
                        return Observable.just(postComments);
                    }
                })
                .subscribe(new Observer<ArrayList<PostComment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showBugSnackBar(e);
                    }

                    @Override
                    public void onNext(ArrayList<PostComment> postComments) {
                        singlePostView.addComments(postComments);
                    }
                });
        //                .subscribeOn(Schedulers.newThread())
        //                .observeOn(AndroidSchedulers.mainThread())
        //                .subscribe(new Observer<CommentResponse>() {
        //                    @Override
        //                    public void onCompleted() {
        //
        //                    }
        //
        //                    @Override
        //                    public void onError(Throwable e) {
        //                        showBugSnackBar(e);
        //                    }
        //
        //                    @Override
        //                    public void onNext(CommentResponse commentResponse) {
        //
        //                    }
        //                });
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
                        Util.sendBugReport(SinglePostActivity.this,
                                getString(R.string.bug_email_subject),
                                getString(R.string.bug_email_context));
                    }
                }).show();
    }

    @Override
    public void onTagClick(CharSequence tag, PrettySpann.TagType tagType) {
        Logger.t("SinglePostActivity").d(tag.toString());
    }
}
