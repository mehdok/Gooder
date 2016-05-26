/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mehdok.gooder.R;
import com.mehdok.gooder.ui.home.PostFunctionHandler;
import com.mehdok.gooder.ui.home.interfaces.PostFunctionListener;
import com.mehdok.gooder.ui.home.models.ParcelablePost;
import com.mehdok.gooder.ui.home.navigation.MainActivityDelegate;
import com.mehdok.gooder.ui.singlepost.SinglePostActivity;
import com.mehdok.gooderapilib.models.post.APIPost;
import com.mehdok.gooderapilib.models.post.AddPost;
import com.mehdok.singlepostviewlib.interfaces.PostMoreListener;
import com.mehdok.singlepostviewlib.interfaces.UserProfileClickListener;
import com.mehdok.singlepostviewlib.models.PostBody;
import com.mehdok.singlepostviewlib.models.PostDetail;
import com.mehdok.singlepostviewlib.utils.ClipBoardUtil;
import com.mehdok.singlepostviewlib.utils.PrettySpann;
import com.mehdok.singlepostviewlib.views.PostBodyView;
import com.mehdok.singlepostviewlib.views.PostDetailView;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by mehdok on 4/13/2016.
 */
public class SinglePostAdapter extends RecyclerView.Adapter<SinglePostAdapter.ItemViewHolder>
        implements PostFunctionListener, PrettySpann.TagClickListener, PostMoreListener {
    private ArrayList<APIPost> mPosts;
    private int BODY_COUNT = 200;
    private PostFunctionHandler functionHandler;
    private UserProfileClickListener userProfileClickListener;
    private WeakReference<Context> mContext;

    public SinglePostAdapter(Context ctx, ArrayList<APIPost> posts,
                             UserProfileClickListener userProfileClickListener) {
        mPosts = posts;
        functionHandler = new PostFunctionHandler(ctx);
        functionHandler.setListener(this);// TODO REMOVE LISTENER ON PAUSE
        this.userProfileClickListener = userProfileClickListener;
        mContext = new WeakReference<Context>(ctx);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_single_post, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if (mPosts.get(position).getTitle().isEmpty()) {
            holder.postTitle.setVisibility(View.GONE);
        } else {
            holder.postTitle.setVisibility(View.VISIBLE);
        }

        holder.postTitle.setText(mPosts.get(position).getTitle());

        holder.postDetail.hideUserPhoto();
        holder.postDetail.setPostDetail(new PostDetail(mPosts.get(position).getAuthor().getUid(),
                mPosts.get(position).getAuthor().getFullName(), mPosts.get(position).getTime(),
                        userProfileClickListener), PostDetailView.More.POST, this, null, null, position,
                null);

        holder.postBody.setPostBody(new PostBody(getLimitedText(mPosts.get(position).getPostBody()),
                getLimitedText(mPosts.get(position).getExtra().getNote()),
                this));

        holder.likeCount.setText(getCount(mPosts.get(position).getLikeCounts()));
        holder.shareCount.setText(getCount(mPosts.get(position).getSharesCount()));
        holder.commentCount.setText(getCount(mPosts.get(position).getCommentCount()));

        if (mPosts.get(position).isLiked()) {
            holder.likeButton.setImageResource(R.drawable.ic_favorite_grey600_24dp);
        } else {
            holder.likeButton.setImageResource(R.drawable.ic_favorite_outline_grey600_24dp);
        }

        if (mPosts.get(position).isStarred()) {
            holder.starButton.setImageResource(R.drawable.ic_star_grey600_24dp);
        } else {
            holder.starButton.setImageResource(R.drawable.ic_star_outline_grey600_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    private String getCount(String count) {
        if (count.equals("0")) {
            return "";
        } else {
            return count;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public PostDetailView postDetail;
        public TextView postTitle;
        public PostBodyView postBody;
        public TextView likeCount;
        public TextView shareCount;
        public TextView commentCount;
        public ImageButton likeButton;
        public ImageButton starButton;
        public ImageButton shareButton;

        public ItemViewHolder(View view) {
            super(view);

            postDetail = (PostDetailView) view.findViewById(R.id.post_detail_view);
            postTitle = (TextView) view.findViewById(R.id.post_title);
            postBody = (PostBodyView) view.findViewById(R.id.post_body);
            likeCount = (TextView) view.findViewById(R.id.like_count);
            shareCount = (TextView) view.findViewById(R.id.share_count);
            commentCount = (TextView) view.findViewById(R.id.comment_count);
            likeButton = (ImageButton) view.findViewById(R.id.like_button);
            starButton = (ImageButton) view.findViewById(R.id.star_button);
            shareButton = (ImageButton) view.findViewById(R.id.share_button);

            view.setOnClickListener(this);
            likeButton.setOnClickListener(this);
            starButton.setOnClickListener(this);
            shareButton.setOnClickListener(this);
            postBody.setClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();

            if (view.getId() == R.id.like_button) {
                if (toggleLike(pos)) {
                    functionHandler.likePost(pos, mPosts.get(pos).getPid());
                } else {
                    functionHandler.unLikePost(pos, mPosts.get(pos).getPid());
                }
            } else if (view.getId() == R.id.star_button) {
                if (toggleStar(pos)) {
                    functionHandler.starPost(pos, mPosts.get(pos).getPid());
                } else {
                    functionHandler.unStarPost(pos, mPosts.get(pos).getPid());
                }
            } else if (view.getId() == R.id.share_button) {
                functionHandler.showNoteDialog(view.getContext(), pos, mPosts.get(pos).getPid());
            } else {
                Intent intent = new Intent(view.getContext(), SinglePostActivity.class);
                intent.putExtra(SinglePostActivity.PARCELABLE_POST_EXTRA,
                        new ParcelablePost(mPosts.get(pos)));
                view.getContext().startActivity(intent);
            }
        }
    }

    private void like(int pos, boolean like) {
        mPosts.get(pos).setLiked(like);
        changeLikeCount(pos, like);
        notifyDataSetChanged();
    }

    private void star(int pos, boolean star) {
        mPosts.get(pos).setStarred(star);
        notifyDataSetChanged();
    }

    private boolean toggleLike(int pos) {
        boolean like = !mPosts.get(pos).isLiked();
        mPosts.get(pos).setLiked(like);
        notifyDataSetChanged();
        return like;
    }

    private boolean toggleStar(int pos) {
        boolean star = !mPosts.get(pos).isStarred();
        mPosts.get(pos).setStarred(star);
        notifyDataSetChanged();
        return star;
    }

    private void changeLikeCount(int pos, boolean increase) {
        if (increase) {
            mPosts.get(pos)
                    .setLikeCounts(Integer.valueOf(mPosts.get(pos).getLikeCounts()) + 1 + "");
        } else {
            mPosts.get(pos)
                    .setLikeCounts(Integer.valueOf(mPosts.get(pos).getLikeCounts()) - 1 + "");
        }
    }

    @Override
    public void onLike(int position, boolean like) {
        like(position, like);
    }

    @Override
    public void onStar(int position, boolean star) {
        star(position, star);
    }

    @Override
    public void onLikeError(int position, Throwable e) {
        toggleLike(position);
        e.printStackTrace();
        MainActivityDelegate.getInstance().getActivity().showBugSnackBar(e);
    }

    @Override
    public void onStarError(int position, Throwable e) {
        toggleStar(position);
        e.printStackTrace();
        MainActivityDelegate.getInstance().getActivity().showBugSnackBar(e);
    }

    @Override
    public void onReShare(int position, AddPost result) {
        MainActivityDelegate.getInstance().getActivity().showSimpleMessage(result.getMsgText());
    }

    @Override
    public void onReShareError(int position, Throwable e) {
        e.printStackTrace();
        MainActivityDelegate.getInstance().getActivity().showBugSnackBar(e);
    }

    @Override
    public void onAddComment(String commentBody) {
        // not used
    }

    @Override
    public void onAddCommentError(Throwable e) {
        // not used
    }

    @Override
    public void onTagClick(CharSequence tag, PrettySpann.TagType tagType) {
        Logger.t("SinglePostAdapter").d(tag.toString());
    }

    private String getLimitedText(String str) {
        if (str == null) return null;

        if (str.length() <= BODY_COUNT) {
            return str;
        } else {
            return str.substring(0, BODY_COUNT) + "<br/>&#x25BC;";
        }
    }

    @Override
    public void copyPostId(int position) {
        ClipBoardUtil.copyText(mContext.get(), mPosts.get(position).getPid());
        showToast(com.mehdok.singlepostviewlib.R.string.clip_post_id);
    }

    @Override
    public void copyPostAuthorId(int position) {
        ClipBoardUtil.copyText(mContext.get(), mPosts.get(position).getAuthor().getUid());
        showToast(com.mehdok.singlepostviewlib.R.string.clip_post_author);
    }

    @Override
    public void copyPostText(int position) {
        ClipBoardUtil.copyText(mContext.get(),
                mPosts.get(position).getPostBody() + mPosts.get(position).getExtra().getNote());
        showToast(com.mehdok.singlepostviewlib.R.string.clip_post_body);
    }

    private void showToast(int resourceId) {
        Toast.makeText(mContext.get(), resourceId, Toast.LENGTH_SHORT).show();
    }
}
