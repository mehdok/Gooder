/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mehdok.gooder.R;
import com.mehdok.gooder.ui.home.PostFunctionHandler;
import com.mehdok.gooder.ui.home.interfaces.PostFunctionListener;
import com.mehdok.gooder.ui.home.models.PrettyPost;
import com.mehdok.gooder.ui.home.navigation.MainActivityDelegate;
import com.mehdok.gooder.views.LinkifyTextView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * Created by mehdok on 4/13/2016.
 */
public class SinglePostAdapter extends RecyclerView.Adapter<SinglePostAdapter.ItemViewHolder>
        implements PostFunctionListener
{
    private ArrayList<PrettyPost> mPosts;
    private int BODY_COUNT = 200;
    PostFunctionHandler functionHandler;

    public SinglePostAdapter(Context ctx, ArrayList<PrettyPost> posts)
    {
        mPosts = posts;
        functionHandler = new PostFunctionHandler(ctx);
        functionHandler.setListener(this);// TODO REMOVE LISTENER ON PAUSE
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_post, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position)
    {
        holder.postAuthor.setText(mPosts.get(position).getAuthor().getFullName());
        holder.postTitle.setText(mPosts.get(position).getTitle());
        holder.postDate.setText(mPosts.get(position).getTime());
        //holder.postBody.setText(mPosts.get(position).getPostBody());// TODO limit text size
        holder.postBody.setPrettyText(mPosts.get(position).getPostBody());
        holder.likeCount.setText(getCount(mPosts.get(position).getLikeCounts()));
        holder.shareCount.setText(getCount(mPosts.get(position).getSharesCount()));
        holder.commentCount.setText(getCount(mPosts.get(position).getCommentCount()));

        if (mPosts.get(position).isLiked()) {
            holder.likeButton.setImageResource(R.drawable.ic_favorite_grey600_24dp);
        } else {
            holder.likeButton.setImageResource(R.drawable.ic_favorite_outline_grey600_24dp);
        }

        if (mPosts.get(position).isStared()) {
            holder.starButton.setImageResource(R.drawable.ic_star_grey600_24dp);
        } else {
            holder.starButton.setImageResource(R.drawable.ic_star_outline_grey600_24dp);
        }
    }

    @Override
    public int getItemCount()
    {
        return mPosts.size();
    }

    private String getCount(String count)
    {
        if (count.equals("0")) {
            return "";
        } else {
            return count;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView postAuthor;
        public TextView postTitle;
        public TextView postDate;
        public LinkifyTextView postBody;
        public TextView likeCount;
        public TextView shareCount;
        public TextView commentCount;
        public ImageButton likeButton;
        public ImageButton starButton;
        public ImageButton shareButton;

        public ItemViewHolder(View view)
        {
            super(view);

            postAuthor = (TextView) view.findViewById(R.id.post_author);
            postTitle = (TextView) view.findViewById(R.id.post_title);
            postDate = (TextView) view.findViewById(R.id.post_date);
            postBody = (LinkifyTextView) view.findViewById(R.id.post_body);
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
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.like_button) {
                final int pos = getAdapterPosition();
                if (toggleLike(pos)) {
                    functionHandler.likePost(pos, mPosts.get(pos).getPid());
                } else {
                    functionHandler.unLikePost(pos, mPosts.get(pos).getPid());
                }

                Logger.t("ItemViewHolder").d("like_button - pos :" + pos);
            } else if (view.getId() == R.id.star_button) {
                final int pos = getAdapterPosition();
                if (toggleStar(pos)) {
                    functionHandler.starPost(pos, mPosts.get(pos).getPid());
                } else {
                    functionHandler.unStarPost(pos, mPosts.get(pos).getPid());
                }

                Logger.t("ItemViewHolder").d("star_button - pos :" + pos);
            } else if (view.getId() == R.id.share_button) {
                Logger.t("ItemViewHolder").d("share_button - pos :" + getAdapterPosition());
            } else {
                Logger.t("ItemViewHolder").d("pos :" + getAdapterPosition());
            }
        }
    }

    private void like(int pos, boolean like) {
        mPosts.get(pos).setLiked(like);
        changeLikeCount(pos, like);
        notifyDataSetChanged();
    }

    private void star(int pos, boolean star) {
        mPosts.get(pos).setStared(star);
        notifyDataSetChanged();
    }

    private boolean toggleLike(int pos) {
        boolean like = !mPosts.get(pos).isLiked();
        mPosts.get(pos).setLiked(like);
        notifyDataSetChanged();
        return like;
    }

    private boolean toggleStar(int pos) {
        boolean star = !mPosts.get(pos).isStared();
        mPosts.get(pos).setStared(star);
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
}
