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
import com.mehdok.gooder.ui.home.PostFunctionHandler;
import com.mehdok.gooder.ui.home.interfaces.PostFunctionListener;
import com.mehdok.gooder.ui.home.models.ParcelablePost;
import com.mehdok.gooder.utils.Util;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.comment.CommentContent;
import com.mehdok.gooderapilib.models.comment.CommentResponse;
import com.mehdok.gooderapilib.models.post.AddPost;
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
        SendCommentClickListener, PrettySpann.TagClickListener, PostFunctionListener {

    public static final String PARCELABLE_POST_EXTRA = "parcelable_post_extra";

    private SinglePostView singlePostView;
    private CoordinatorLayout mRootLayout;
    private PostFunctionHandler functionHandler;
    private ParcelablePost post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        singlePostView = (SinglePostView) findViewById(R.id.single_post_view);
        mRootLayout = (CoordinatorLayout) findViewById(R.id.single_post_root_layout);

        setUpToolbar();
        post = getExtra();
        showPost(post);

        functionHandler = new PostFunctionHandler(this);
        functionHandler.setListener(this);// TODO REMOVE LISTENER ON PAUSE
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.single_post_toolbar);
        toolbar.setTitle("");
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

        correctLikeIcon(post.isLiked());
        correctStarIcon(post.isStared());
    }

    @Override
    public void likeClicked() {
        if (toggleLike()) {
            functionHandler.likePost(0, post.getPid());
        } else {
            functionHandler.unLikePost(0, post.getPid());
        }
    }

    @Override
    public void shareClicked() {
        functionHandler.showNoteDialog(this, 0, post.getPid());
    }

    @Override
    public void starClicked() {
        if (toggleStar()) {
            functionHandler.starPost(0, post.getPid());
        } else {
            functionHandler.unStarPost(0, post.getPid());
        }
    }

    @Override
    public void sendComment(String comment) {
        functionHandler.addComment(post.getPid(), comment);
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

    public void showSimpleMessage(String str) {
        Snackbar.make(mRootLayout, str, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onTagClick(CharSequence tag, PrettySpann.TagType tagType) {
        Logger.t("SinglePostActivity").d(tag.toString());
    }

    @Override
    public void onLike(int position, boolean like) {
        like(like);
    }

    @Override
    public void onStar(int position, boolean star) {
        star(star);
    }

    @Override
    public void onLikeError(int position, Throwable e) {
        toggleLike();
        e.printStackTrace();
        showBugSnackBar(e);
    }

    @Override
    public void onStarError(int position, Throwable e) {
        toggleStar();
        e.printStackTrace();
        showBugSnackBar(e);
    }

    @Override
    public void onReShare(int position, AddPost result) {
        showSimpleMessage(result.getMsgText());
    }

    @Override
    public void onReShareError(int position, Throwable e) {
        e.printStackTrace();
        showBugSnackBar(e);
    }

    @Override
    public void onAddComment(String commentBody) {
        post.setCommentCount(Integer.valueOf(post.getCommentCount()) + 1 + "");
        singlePostView.changeCommentCount(Integer.valueOf(post.getCommentCount()));
        //TODO add comment to view
        UserInfo userInfo = DatabaseHelper.getInstance(this).getUserInfo();
        PostComment postComment =
                new PostComment(userInfo.getFullname(), System.currentTimeMillis() + "",
                        commentBody, userInfo.getAvatar());
        singlePostView.addComment(postComment);
    }

    @Override
    public void onAddCommentError(Throwable e) {
        e.printStackTrace();
        showBugSnackBar(e);
    }

    private void like(boolean like) {
        post.setLiked(like);
        changeLikeCount(like);
    }

    private void changeLikeCount(boolean increase) {
        if (increase) {
            post.setLikeCounts(Integer.valueOf(post.getLikeCounts()) + 1 + "");
        } else {
            post.setLikeCounts(Integer.valueOf(post.getLikeCounts()) - 1 + "");
        }

        singlePostView.changeLikeCount(Integer.valueOf(post.getLikeCounts()));
    }

    private boolean toggleLike() {
        boolean like = !post.isLiked();
        post.setLiked(like);
        correctLikeIcon(like);
        return like;
    }

    private void star(boolean star) {
        post.setStared(star);
    }

    private boolean toggleStar() {
        boolean star = !post.isStared();
        post.setStared(star);
        correctStarIcon(star);
        return star;
    }

    private void correctStarIcon(boolean star) {
        if (star) {
            singlePostView.changeStarIcon(R.drawable.ic_star_grey600_24dp);
        } else {
            singlePostView.changeStarIcon(R.drawable.ic_star_outline_grey600_24dp);
        }
    }

    private void correctLikeIcon(boolean like) {
        if (like) {
            singlePostView.changeLikeIcon(R.drawable.ic_favorite_grey600_24dp);
        } else {
            singlePostView.changeLikeIcon(R.drawable.ic_favorite_outline_grey600_24dp);
        }
    }
}