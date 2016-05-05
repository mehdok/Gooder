/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooderapilib;

import com.mehdok.gooderapilib.models.AccessCode;
import com.mehdok.gooderapilib.models.BaseResponse;
import com.mehdok.gooderapilib.models.PremiumUserResponse;
import com.mehdok.gooderapilib.models.addfeed.AddFeedResponse;
import com.mehdok.gooderapilib.models.addgroup.GroupResponse;
import com.mehdok.gooderapilib.models.block.BlockResponse;
import com.mehdok.gooderapilib.models.block.BlockedUser;
import com.mehdok.gooderapilib.models.comment.Comment;
import com.mehdok.gooderapilib.models.comment.CommentResponse;
import com.mehdok.gooderapilib.models.follow.FollowCount;
import com.mehdok.gooderapilib.models.follow.FollowResponse;
import com.mehdok.gooderapilib.models.follow.Followed;
import com.mehdok.gooderapilib.models.follow.Followers;
import com.mehdok.gooderapilib.models.notification.NotificationList;
import com.mehdok.gooderapilib.models.post.AddPost;
import com.mehdok.gooderapilib.models.post.Like;
import com.mehdok.gooderapilib.models.post.NewPostsCount;
import com.mehdok.gooderapilib.models.post.PostReadResponse;
import com.mehdok.gooderapilib.models.post.PostReadsCount;
import com.mehdok.gooderapilib.models.post.Posts;
import com.mehdok.gooderapilib.models.post.SinglePost;
import com.mehdok.gooderapilib.models.user.UserInfo;
import com.mehdok.gooderapilib.models.user.UserSearchInfo;
import com.mehdok.gooderapilib.models.user.Users;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by mehdok on 5/1/2016.
 */
public interface GooderInterface {
    @FormUrlEncoded
    @POST("?method=get-access-code")
    Observable<AccessCode> getAccessCode(@FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=user-info")
    Observable<UserInfo> getUserInfo(@QueryMap Map<String, String> params,
                                     @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=users-timeline")
    Observable<Posts> getAllFriendsItem(@QueryMap Map<String, String> params,
                                        @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=users-info")
    Observable<Users> getUsersInfo(@Query("uids") String uids, @FieldMap Map<String, String> query);

    //TODO site return different result on different situation, when feed in invalid,
    // when it is blocked by gov and when it is successful
    @FormUrlEncoded
    @POST("?method=add-feed")
    Observable<AddFeedResponse> addFeed(@Query("feed_url") String feed,
                                        @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=add-group")
    Observable<GroupResponse> addGroup(@Query("title") String title,
                                       @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=edit-group")
    Observable<GroupResponse> editGroup(@Query("gid") Integer gid,
                                        @Query("title") String title,
                                        @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=get-group")
    Observable<GroupResponse> getGroup(@Query("gid") Integer gid,
                                       @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=delete-group")
    Observable<GroupResponse> deleteGroup(@Query("gid") Integer gid,
                                          @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=move-user-to-group")
    Observable<BaseResponse> moveUserToGroup(@Query("uid") Integer uid,
                                             @Query("gid") Integer gid,
                                             @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=block-user")
    Observable<BlockResponse> blockUser(@Query("uid") Integer uid,
                                        @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=is-blocked-user")
    Observable<BlockResponse> isBlockedUser(@Query("uid") Integer uid,
                                            @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=unblock-user")
    Observable<BlockResponse> unBlockUser(@Query("uid") Integer uid,
                                          @FieldMap Map<String, String> query);

    // usable for blocked-users-count
    @FormUrlEncoded
    @POST("?method=blocked-users")
    Observable<BlockedUser> getBlockedUsers(@FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=follow-user")
    Observable<FollowResponse> followUser(@Query("uid") Integer uid,
                                          @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=unfollow-user")
    Observable<FollowResponse> unFollowUser(@Query("uid") Integer uid,
                                            @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=is-user-my-follower")
    Observable<FollowResponse> isUserMyFollower(@Query("uid") Integer uid,
                                                @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=is-user-followed")
    Observable<FollowResponse> isUserFollowed(@Query("uid") Integer uid,
                                              @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=is-followed-and-follower")
    Observable<FollowResponse> isFollowerAndFollowed(@Query("uid") Integer uid,
                                                     @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=followed-users")
    Observable<Followed> getFollowedUsers(@QueryMap Map<String, String> params,
                                          @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=followed-feeds")
    Observable<Followed> getFollowedFeed(@QueryMap Map<String, String> params,
                                         @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=followed-users-and-feeds")
    Observable<Followed> getFollowedUserAndFeed(@QueryMap Map<String, String> params,
                                                @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=follower-users")
    Observable<Followers> getFollowers(@FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=followed-users-count")
    Observable<FollowCount> getFollowedCounts(@FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=follower-users-count")
    Observable<FollowCount> getFollowerCounts(@FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=user-posts")
    Observable<Posts> getUserPosts(@QueryMap Map<String, String> params,
                                   @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=recommended-posts")
    Observable<Posts> getRecommendedPosts(@QueryMap Map<String, String> params,
                                          @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=random-posts")
    Observable<Posts> getRandomPosts(@QueryMap Map<String, String> params,
                                     @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=commented-posts")
    Observable<Posts> getCommentedPosts(@QueryMap Map<String, String> params,
                                        @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=starred-posts")
    Observable<Posts> getStaredPosts(@QueryMap Map<String, String> params,
                                     @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=general-timeline")
    Observable<Posts> getGeneralTimeLine(@QueryMap Map<String, String> params,
                                         @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=feeds-timeline")
    Observable<Posts> getFeedTimeLine(@QueryMap Map<String, String> params,
                                      @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=search-posts")
    Observable<Posts> searchPosts(@QueryMap Map<String, String> params,
                                  @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=search-users")
    Observable<UserSearchInfo> searchUsers(@QueryMap Map<String, String> params,
                                           @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=search-tags")
    Observable<Posts> searchTags(@QueryMap Map<String, String> params,
                                 @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=add-post")
    Observable<AddPost> addPost(@QueryMap Map<String, String> params,
                                @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=edit-post")
    Observable<AddPost> editPost(@QueryMap Map<String, String> params,
                                 @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=delete-post")
    Observable<BaseResponse> deletePost(@Query("pid") String pid,
                                        @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=reshare-post")
    Observable<AddPost> resharePost(@QueryMap Map<String, String> params,
                                    @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=like-post")
    Observable<AddPost> likePost(@Query("pid") String pid,
                                 @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=unlike-post")
    Observable<AddPost> unLikePost(@Query("pid") String pid,
                                   @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=my-posts-count")
    Observable<FollowCount> getMyPostCount(@FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=my-post-reads-count")
    Observable<PostReadsCount> getMyPostReadsCount(@Query("pid") String pid,
                                                   @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=my-post-reads-count")
    Observable<CommentResponse> getPostComments(@Query("pid") String pid,
                                                @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=add-comment")
    Observable<Comment> addComment(@Query("pid") String pid,
                                   @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=delete-comment")
    Observable<BaseResponse> deleteComment(@Query("cid") String cid,
                                           @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=get-post")
    Observable<SinglePost> getPost(@Query("pid") String pid,
                                   @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=post-shares")
    Observable<Posts> getPostReshares(@Query("pid") String pid,
                                      @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=post-likers")
    Observable<Followers> getPostLikers(@Query("pid") String pid,
                                        @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=did-like-post")
    Observable<Like> didLikePost(@QueryMap Map<String, String> params,
                                 @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=did-star-post")
    Observable<Like> didStarPost(@Query("pid") String pid,
                                 @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=star-post")
    Observable<Like> starPost(@Query("pid") String pid,
                              @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=unstar-post")
    Observable<Like> unStarPost(@Query("pid") String pid,
                                @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=mark-post-as-read")
    Observable<PostReadResponse> markPostAsRead(@Query("pid") String pid,
                                                @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=mark-post-as-unread")
    Observable<Like> markPostAsUnRead(@Query("pid") String pid,
                                      @FieldMap Map<String, String> query);

    //TODO http://gooder.in/api/?method=mark-everything-as-read
    //TODO http://gooder.in/api/?method=mark-user-as-read
    //TODO http://gooder.in/api/?method=delete-notification
    //TODO http://gooder.in/api/?method=clear-notifications

    @FormUrlEncoded
    @POST("?method=new-posts-count")
    Observable<NewPostsCount> getNewPostsCount(@Query("uid") String uid,
                                               @FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=notification-list")
    Observable<NotificationList> getNotification(@FieldMap Map<String, String> query);

    @FormUrlEncoded
    @POST("?method=is-premium-user")
    Observable<PremiumUserResponse> isUserPremium(@FieldMap Map<String, String> query);

}
