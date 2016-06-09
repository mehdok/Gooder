/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mehdok.gooder.R;
import com.mehdok.gooder.ui.home.PostFunctionHandler;
import com.mehdok.gooder.ui.home.interfaces.PostFunctionListener;
import com.mehdok.gooder.ui.home.navigation.MainActivityDelegate;
import com.mehdok.gooder.ui.singlepost.SinglePostActivity;
import com.mehdok.gooder.utils.MehdokTextUtil;
import com.mehdok.gooder.utils.ReshareUtil;
import com.mehdok.gooderapilib.models.post.APIPost;
import com.mehdok.gooderapilib.models.post.AddPost;
import com.mehdok.singlepostviewlib.interfaces.PostMoreListener;
import com.mehdok.singlepostviewlib.interfaces.ReshareBodyClickListener;
import com.mehdok.singlepostviewlib.interfaces.UserProfileClickListener;
import com.mehdok.singlepostviewlib.models.PostBody;
import com.mehdok.singlepostviewlib.models.PostDetail;
import com.mehdok.singlepostviewlib.utils.ClipBoardUtil;
import com.mehdok.singlepostviewlib.utils.PrettySpann;
import com.mehdok.singlepostviewlib.views.PostBodyView;
import com.mehdok.singlepostviewlib.views.PostDetailView;
import com.mehdok.singlepostviewlib.views.PostTextView;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by mehdok on 4/13/2016.
 */
public class SinglePostAdapter extends RecyclerView.Adapter<SinglePostAdapter.ItemViewHolder>
        implements PostFunctionListener, PrettySpann.TagClickListener, PostMoreListener,
        ReshareBodyClickListener {
    private ArrayList<APIPost> mPosts;
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

        holder.postBody.setPostBody(new PostBody(
                MehdokTextUtil.getLimitedText(mPosts.get(position).getPostBody(),
                        MehdokTextUtil.BODY_COUNT),
                MehdokTextUtil.getLimitedText(mPosts.get(position).getExtra().getNote(),
                        MehdokTextUtil.BODY_COUNT),
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

        if (mPosts.get(position).isRead()) {
            holder.readButton.setImageResource(R.drawable.tick_fill);
        } else {
            holder.readButton.setImageResource(R.drawable.tick_empty);
        }

        if (mPosts.get(position).getReshareChains() != null &&
                mPosts.get(position).getReshareChains().size() > 0) {
            ReshareUtil.getReshareChainView(holder.bodyRoot,
                    mPosts.get(position).getReshareChains(), MehdokTextUtil.BODY_COUNT, this,
                    userProfileClickListener, this);
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    private String getCount(String count) {
        if (count == null || count.equals("0")) {
            return "";
        } else {
            return count;
        }
    }

    @Override
    public void onReshareBodyClicked(String resharePostId) {
        openSinglePostActivity(resharePostId);
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
        public ImageButton readButton;
        public LinearLayout bodyRoot;

        // Transitions
        public PostTextView authorName;
        public PostTextView postDate;
        public ImageView authorAvatar;
        public ImageButton commentButton;
        public ImageButton postMoreButton;

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
            readButton = (ImageButton) view.findViewById(R.id.read_button);
            bodyRoot = (LinearLayout) view.findViewById(R.id.body_root_layout);

            view.setOnClickListener(this);
            likeButton.setOnClickListener(this);
            starButton.setOnClickListener(this);
            shareButton.setOnClickListener(this);
            readButton.setOnClickListener(this);
            postBody.setClickListener(this);

            //transition
            authorName = (PostTextView) view.findViewById(R.id.author_name);
            postDate = (PostTextView) view.findViewById(R.id.post_date);
            authorAvatar = (ImageView) view.findViewById(R.id.author_pic);
            commentButton = (ImageButton) view.findViewById(R.id.comment_button);
            postMoreButton = (ImageButton) view.findViewById(R.id.post_more_button);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            int id = view.getId();

            if (id == R.id.like_button) {
                if (toggleLike(pos)) {
                    functionHandler.likePost(pos, mPosts.get(pos).getPid());
                } else {
                    functionHandler.unLikePost(pos, mPosts.get(pos).getPid());
                }
            } else if (id == R.id.star_button) {
                if (toggleStar(pos)) {
                    functionHandler.starPost(pos, mPosts.get(pos).getPid());
                } else {
                    functionHandler.unStarPost(pos, mPosts.get(pos).getPid());
                }
            } else if (id == R.id.share_button) {
                functionHandler.showNoteDialog(view.getContext(), pos, mPosts.get(pos).getPid());
            } else if (id == R.id.read_button) {
                if (toggleRead(pos)) {
                    functionHandler.markPostAsRead(pos, mPosts.get(pos).getPid());
                } else {
                    functionHandler.markPostAsUnread(pos, mPosts.get(pos).getPid());
                }
            } else {
                openSinglePostActivity(mPosts.get(pos).getPid());
                /*Intent intent = new Intent(view.getContext(), SinglePostActivity.class);
                intent.putExtra(SinglePostActivity.POST_ID_EXTRA,
                        mPosts.get(pos).getPid());
                //view.getContext().startActivity(intent);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        ((AppCompatActivity) mContext.get()),

                        new Pair<View, String>(postTitle,
                                view.getContext().getString(R.string.transition_post_title)),
                        new Pair<View, String>(postBody,
                                view.getContext().getString(R.string.transition_post_body)),
                        new Pair<View, String>(postDate,
                                view.getContext().getString(R.string.transition_post_date)),
                        new Pair<View, String>(authorName,
                                view.getContext().getString(R.string.transition_user_name)),
                        new Pair<View, String>(authorAvatar, // FIXME: 6/6/2016 cause inconsistency
                                view.getContext().getString(R.string.transition_user_avatar)),
                        new Pair<View, String>(commentButton,
                                view.getContext().getString(R.string.transition_function_comment)),
                        new Pair<View, String>(readButton,
                                view.getContext().getString(R.string.transition_function_read)),
                        new Pair<View, String>(likeButton,
                                view.getContext().getString(R.string.transition_function_like)),
                        new Pair<View, String>(starButton,
                                view.getContext().getString(R.string.transition_function_star)),
                        new Pair<View, String>(shareButton,
                                view.getContext().getString(R.string.transition_function_share)),
                        new Pair<View, String>(postMoreButton,
                                view.getContext().getString(R.string.transition_post_more)),
                        new Pair<View, String>(likeCount,
                                view.getContext()
                                        .getString(R.string.transition_function_like_count)),
                        new Pair<View, String>(shareCount,
                                view.getContext()
                                        .getString(R.string.transition_function_share_count)),
                        new Pair<View, String>(commentCount,
                                view.getContext()
                                        .getString(R.string.transition_function_comment_count))
                );
                ActivityCompat.startActivity((AppCompatActivity) mContext.get(),
                        intent, options.toBundle());*/
            }
        }
    }

    private void openSinglePostActivity(String postId) {
        Intent intent = new Intent(mContext.get(), SinglePostActivity.class);
        intent.putExtra(SinglePostActivity.POST_ID_EXTRA, postId);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                ((AppCompatActivity) mContext.get()), null);
        ActivityCompat.startActivity((AppCompatActivity) mContext.get(),
                intent, options.toBundle());
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

    private boolean toggleRead(int pos) {
        boolean read = !mPosts.get(pos).isRead();
        mPosts.get(pos).setRead(read);
        notifyDataSetChanged();
        return read;
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
    public void onRead(int position, boolean read) {
        //TODO
    }

    @Override
    public void onReadError(int position, Throwable e) {
        toggleRead(position);
        e.printStackTrace();
        MainActivityDelegate.getInstance().getActivity().showBugSnackBar(e);
    }

    @Override
    public void onUnRead(int position, boolean read) {
        //TODO
    }

    @Override
    public void onUnReadError(int position, Throwable e) {
        toggleRead(position);
        e.printStackTrace();
        MainActivityDelegate.getInstance().getActivity().showBugSnackBar(e);
    }

    @Override
    public void onTagClick(CharSequence tag, PrettySpann.TagType tagType) {
        Logger.t("SinglePostAdapter").d(tag.toString());
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
