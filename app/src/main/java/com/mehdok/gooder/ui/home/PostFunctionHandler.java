/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mehdok.gooder.R;
import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.database.DatabaseHelper;
import com.mehdok.gooder.ui.home.interfaces.PostFunctionListener;
import com.mehdok.gooder.views.VazirButton;
import com.mehdok.gooder.views.VazirEditText;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.comment.Comment;
import com.mehdok.gooderapilib.models.post.AddPost;
import com.mehdok.gooderapilib.models.post.Like;
import com.mehdok.gooderapilib.models.post.PostReadResponse;
import com.mehdok.gooderapilib.models.user.UserInfo;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mehdok on 5/3/2016.
 */
public class PostFunctionHandler {
    private RequestBuilder requestBuilder;
    private QueryBuilder queryBuilder;
    private PostFunctionListener listener;

    public PostFunctionHandler(Context ctx) {
        UserInfo userInfo = DatabaseHelper.getInstance(ctx).getUserInfo();
        if (userInfo == null) {
            Toast.makeText(ctx, R.string.not_logged_in, Toast.LENGTH_SHORT).show();
            return;
        }

        requestBuilder = new RequestBuilder();
        queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(userInfo.getUsername());
        try {
            queryBuilder.setPassword(Crypto.getMD5BASE64(
                    new String(Crypto.decrypt(userInfo.getPassword(), ctx))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void likePost(final int position, String pid) {
        requestBuilder.likePost(pid, queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddPost>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onLikeError(position, e);
                        }
                    }

                    @Override
                    public void onNext(AddPost addPost) {
                        if (listener != null) {
                            listener.onLike(position, true);
                        }
                    }
                });
    }

    public void unLikePost(final int position, String pid) {
        requestBuilder.unLikePost(pid, queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddPost>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onLikeError(position, e);
                        }
                    }

                    @Override
                    public void onNext(AddPost addPost) {
                        if (listener != null) {
                            listener.onLike(position, false);
                        }
                    }
                });
    }

    public void starPost(final int position, String pid) {
        requestBuilder.starPost(pid, queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Like>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onStarError(position, e);
                        }
                    }

                    @Override
                    public void onNext(Like like) {
                        if (listener != null) {
                            listener.onStar(position, true);
                        }
                    }
                });
    }

    public void unStarPost(final int position, String pid) {
        requestBuilder.unStarPost(pid, queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Like>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onStarError(position, e);
                        }
                    }

                    @Override
                    public void onNext(Like like) {
                        if (listener != null) {
                            listener.onStar(position, false);
                        }
                    }
                });
    }

    public void reSharePost(final int position, String pid, String noteBody) {
        queryBuilder.setPid(pid);
        queryBuilder.setNoteBody(noteBody);
        requestBuilder.resharePost(queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddPost>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onReShareError(position, e);
                        }
                    }

                    @Override
                    public void onNext(AddPost addPost) {
                        if (listener != null) {
                            listener.onReShare(position, addPost);
                        }
                    }
                });
    }

    public void addComment(String pid, final String commentBody) {
        queryBuilder.setPid(pid);
        queryBuilder.setCommentBody(commentBody);
        requestBuilder.addComment(pid, queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Comment>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onAddCommentError(e);
                        }
                    }

                    @Override
                    public void onNext(Comment comment) {
                        if (listener != null) {
                            listener.onAddComment(commentBody);
                        }
                    }
                });
    }

    public void showNoteDialog(Context context, final int position, final String pid) {
        final AppCompatDialog shareDialog = new AppCompatDialog(context, R.style.DialogStyle);
        shareDialog.setContentView(R.layout.dialog_note);
        shareDialog.show();
        shareDialog.getWindow()
                .setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        final VazirEditText note = (VazirEditText) shareDialog.findViewById(R.id.reshare_note);
        VazirButton reShareButton = (VazirButton) shareDialog.findViewById(R.id.reshare_do);
        VazirButton cancelButton = (VazirButton) shareDialog.findViewById(R.id.reshare_cancel);
        reShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reSharePost(position, pid, note.getText().toString());
                shareDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDialog.dismiss();
            }
        });
    }

    public void setListener(PostFunctionListener listener) {
        this.listener = listener;
    }

    public void markPostAsRead(final int position, String pid) {
        requestBuilder.markPostAsRead(pid, queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostReadResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onReadError(position, e);
                        }
                    }

                    @Override
                    public void onNext(PostReadResponse postReadResponse) {
                        if (listener != null) {
                            listener.onRead(position, true);
                        }
                    }
                });
    }

    public void markPostAsUnread(final int position, String pid) {
        requestBuilder.markPostAsUnRead(pid, queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Like>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onUnReadError(position, e);
                        }
                    }

                    @Override
                    public void onNext(Like like) {
                        if (listener != null) {
                            listener.onUnRead(position, false);
                        }
                    }
                });
    }
}
