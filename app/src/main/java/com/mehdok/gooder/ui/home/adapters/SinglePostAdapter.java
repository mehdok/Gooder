/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mehdok.gooder.R;
import com.mehdok.gooder.network.model.Post;

import java.util.ArrayList;

/**
 * Created by mehdok on 4/13/2016.
 */
public class SinglePostAdapter extends RecyclerView.Adapter<SinglePostAdapter.ItemViewHolder>
{
    private ArrayList<Post> mPosts;
    private int BODY_COUNT = 200;

    public SinglePostAdapter(ArrayList<Post> posts)
    {
        mPosts = posts;
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
        holder.postBody.setText(mPosts.get(position).getPostBody());// TODO limit text size
        //holder.postBody.setMovementMethod(LinkMovementMethod.getInstance());
        holder.likeCount.setText(getCount(mPosts.get(position).getLikesCount()));
        holder.shareCount.setText(getCount(mPosts.get(position).getSharesCount()));
        holder.commentCount.setText(getCount(mPosts.get(position).getCommentsCount()));
    }

    @Override
    public int getItemCount()
    {
        return mPosts.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder
    {
        public TextView postAuthor;
        public TextView postTitle;
        public TextView postDate;
        public TextView postBody;
        public TextView likeCount;
        public TextView shareCount;
        public TextView commentCount;

        public ItemViewHolder(View view)
        {
            super(view);

            postAuthor = (TextView)view.findViewById(R.id.post_author);
            postTitle = (TextView)view.findViewById(R.id.post_title);
            postDate = (TextView)view.findViewById(R.id.post_date);
            postBody = (TextView)view.findViewById(R.id.post_body);
            likeCount = (TextView)view.findViewById(R.id.like_count);
            shareCount = (TextView)view.findViewById(R.id.share_count);
            commentCount = (TextView)view.findViewById(R.id.comment_count);
        }
    }

    private String getCount(String count)
    {
        if (count.equals("0"))
            return "";
        else
            return count;
    }
}
