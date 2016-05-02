/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok;

import com.mehdok.models.AccessCode;
import com.mehdok.models.BaseResponse;
import com.mehdok.models.PremiumUserResponse;
import com.mehdok.models.addfeed.AddFeedResponse;
import com.mehdok.models.addgroup.GroupResponse;
import com.mehdok.models.block.BlockResponse;
import com.mehdok.models.block.BlockedUser;
import com.mehdok.models.comment.Comment;
import com.mehdok.models.follow.FollowCount;
import com.mehdok.models.follow.FollowResponse;
import com.mehdok.models.follow.Followed;
import com.mehdok.models.follow.Followers;
import com.mehdok.models.notification.NotificationList;
import com.mehdok.models.post.AddPost;
import com.mehdok.models.post.Like;
import com.mehdok.models.post.NewPostsCount;
import com.mehdok.models.post.PostReadResponse;
import com.mehdok.models.post.PostReadsCount;
import com.mehdok.models.post.Posts;
import com.mehdok.models.post.SinglePost;
import com.mehdok.models.user.UserInfo;
import com.mehdok.models.user.UserSearchInfo;
import com.mehdok.models.user.Users;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by mehdok on 5/1/2016.
 */
public class RequestBuilder {
    private GooderInterface mGooderApi;

    public RequestBuilder() {
        mGooderApi = GooderApi.getInstance().getApi();
    }

    /**
     * @param queryBuilder
     * @return user info
     */
    public Observable<UserInfo> getUserInfo(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<UserInfo>>() {
                    @Override
                    public Observable<UserInfo> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getUserInfo(queryBuilder.getUserInfoParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * @param queryBuilder
     * @return n number of friends item
     */
    public Observable<Posts> getAllFriendsItem(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Posts>>() {
                    @Override
                    public Observable<Posts> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getAllFriendsItem(queryBuilder.getUsersTimeLineParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * @param uids         comma delimited ids, like: 1,2,3,4
     * @param queryBuilder
     * @return
     */
    public Observable<Users> getUsersInfo(final String uids, final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Users>>() {
                    @Override
                    public Observable<Users> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getUsersInfo(uids, queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * if message type is error, something went wrong
     * <p/>
     * this might call onError with HTTP 403 Forbidden
     *
     * @param feedUrl
     * @param queryBuilder
     * @return
     */
    public Observable<AddFeedResponse> addFeed(final String feedUrl,
                                               final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<AddFeedResponse>>() {
                    @Override
                    public Observable<AddFeedResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.addFeed(feedUrl, queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * @param title        group title
     * @param queryBuilder
     * @return
     */
    public Observable<GroupResponse> addGroup(final String title,
                                              final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<GroupResponse>>() {
                    @Override
                    public Observable<GroupResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.addGroup(title, queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * @param gid          id of editing group
     * @param title        new title
     * @param queryBuilder
     * @return
     */
    public Observable<GroupResponse> editGroup(final Integer gid,
                                               final String title,
                                               final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<GroupResponse>>() {
                    @Override
                    public Observable<GroupResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.editGroup(gid, title, queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * @param gid          group id to get
     * @param queryBuilder
     * @return
     */
    public Observable<GroupResponse> getGroup(final Integer gid,
                                              final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<GroupResponse>>() {
                    @Override
                    public Observable<GroupResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getGroup(gid, queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * @param gid          group id to delete
     * @param queryBuilder
     * @return
     */
    public Observable<GroupResponse> deleteGroup(final Integer gid,
                                                 final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<GroupResponse>>() {
                    @Override
                    public Observable<GroupResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.deleteGroup(gid, queryBuilder.getPostParams());
                    }
                });
    }

    /**
     *
     * @param uid user id to move
     * @param gid group id move to
     * @param queryBuilder
     * @return
     */
    public Observable<BaseResponse> moveUserToGroup(final Integer uid,
                                                    final Integer gid,
                                                    final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<BaseResponse>>() {
                    @Override
                    public Observable<BaseResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.moveUserToGroup(uid,
                                gid,
                                queryBuilder.getPostParams());
                    }
                });
    }

    /**
     *
     * @param uid user id to block
     * @param queryBuilder
     * @return
     */
    public Observable<BlockResponse> blockUser(final Integer uid,
                                               final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<BlockResponse>>() {
                    @Override
                    public Observable<BlockResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.blockUser(uid, queryBuilder.getPostParams());
                    }
                });
    }

    /**
     *
     * @param uid user id to check
     * @param queryBuilder
     * @return
     */
    public Observable<BlockResponse> isBlockedUser(final Integer uid,
                                               final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<BlockResponse>>() {
                    @Override
                    public Observable<BlockResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.isBlockedUser(uid, queryBuilder.getPostParams());
                    }
                });
    }

    /**
     *
     * @param uid user id to unblock
     * @param queryBuilder
     * @return
     */
    public Observable<BlockResponse> unBlockUser(final Integer uid,
                                                   final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<BlockResponse>>() {
                    @Override
                    public Observable<BlockResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.unBlockUser(uid, queryBuilder.getPostParams());
                    }
                });
    }

    // TODO this is not working du to site bug
    public Observable<BlockedUser> getBlockedUsers(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<BlockedUser>>() {
                    @Override
                    public Observable<BlockedUser> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getBlockedUsers(queryBuilder.getPostParams());
                    }
                });
    }

    /**
     *
     * @param uid user to follow
     * @param queryBuilder
     * @return
     */
    public Observable<FollowResponse> followUser(final Integer uid,
                                                 final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<FollowResponse>>() {
                    @Override
                    public Observable<FollowResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.followUser(uid, queryBuilder.getPostParams());
                    }
                });
    }

    /**
     *
     * @param uid user to unfollow
     * @param queryBuilder
     * @return
     */
    public Observable<FollowResponse> unFollowUser(final Integer uid,
                                                 final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<FollowResponse>>() {
                    @Override
                    public Observable<FollowResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.unFollowUser(uid, queryBuilder.getPostParams());
                    }
                });
    }

    /**
     *
     * @param uid user id to check
     * @param queryBuilder
     * @return
     */
    public Observable<FollowResponse> isUserMyFollower(final Integer uid,
                                                   final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<FollowResponse>>() {
                    @Override
                    public Observable<FollowResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.isUserMyFollower(uid, queryBuilder.getPostParams());
                    }
                });
    }

    /**
     *
     * @param uid user id to check
     * @param queryBuilder
     * @return
     */
    public Observable<FollowResponse> isUserFollowed(final Integer uid,
                                                       final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<FollowResponse>>() {
                    @Override
                    public Observable<FollowResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.isUserFollowed(uid, queryBuilder.getPostParams());
                    }
                });
    }

    /**
     *
     * @param uid user id to check
     * @param queryBuilder
     * @return
     */
    public Observable<FollowResponse> isFollowerAndFollowed(final Integer uid,
                                                     final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<FollowResponse>>() {
                    @Override
                    public Observable<FollowResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.isFollowerAndFollowed(uid, queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<Followed> getFollowedUsers(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Followed>>() {
                    @Override
                    public Observable<Followed> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getFollowedUsers(
                                queryBuilder.getFollowedUsersParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<Followed> getFollowedFeed(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Followed>>() {
                    @Override
                    public Observable<Followed> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getFollowedFeed(
                                queryBuilder.getFollowedUsersParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<Followed> getFollowedUserAndFeed(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Followed>>() {
                    @Override
                    public Observable<Followed> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getFollowedUserAndFeed(
                                queryBuilder.getFollowedUsersParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<Followers> getFollowers(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Followers>>() {
                    @Override
                    public Observable<Followers> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getFollowers(queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<FollowCount> getFollowedCounts(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<FollowCount>>() {
                    @Override
                    public Observable<FollowCount> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getFollowedCounts(queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<FollowCount> getFollowerCounts(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<FollowCount>>() {
                    @Override
                    public Observable<FollowCount> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getFollowerCounts(queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * all get params are optional
     * uid: if not provided, the authenticated user's id will be used.
     * start: start offset. Use for pagination.
     * unread_only: Set this parameter to 1 to return only unread posts.
     * reverse_order: Set this parameter to 1 to return older posts first.
     *
     * @param queryBuilder
     * @return
     */
    public Observable<Posts> getUserPosts(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Posts>>() {
                    @Override
                    public Observable<Posts> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getUserPosts(queryBuilder.getUserPostsParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * all get params are optional
     *
     * type: The factor which the recommendation will be based on. Can be one of likes, shares,
     * or comments. If not provided, likes will be used.
     * start: Start offset. Use for pagination.
     *
     * @param queryBuilder
     * @return
     */
    public Observable<Posts> getRecommendedPosts(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Posts>>() {
                    @Override
                    public Observable<Posts> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getRecommendedPosts(queryBuilder.getRecommendedPostsParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * all get params are optional
     *
     * start: Start offset. Use for pagination.
     * @param queryBuilder
     * @return
     */
    //TODO this is not working du to site bug
    public Observable<Posts> getRandomPosts(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Posts>>() {
                    @Override
                    public Observable<Posts> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getRandomPosts(queryBuilder.getRandomPostsParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * all get params are optional
     *
     * type: The factor to filter the commented posts based on. Can be one of me,
     * me-friends, or my-posts. If not provided, me will be used.
     * start: Start offset. Use for pagination.
     *
     * @param queryBuilder
     * @return
     */
    public Observable<Posts> getCommentedPosts(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Posts>>() {
                    @Override
                    public Observable<Posts> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getCommentedPosts(queryBuilder.getCommentedPostsParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * start: Start offset. Use for pagination.
     *
     * @param queryBuilder
     * @return
     */
    public Observable<Posts> getStaredPosts(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Posts>>() {
                    @Override
                    public Observable<Posts> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getStaredPosts(queryBuilder.getStaredItemParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * gid: If provided, will filter the results to the provided group id.
     * start: Start offset. Use for pagination.
     * unread_only: Set this parameter to 1 to return only unread posts.
     * reverse_order: Set this parameter to 1 to return older posts first.
     *
     * @param queryBuilder
     * @return
     */
    public Observable<Posts> getGeneralTimeLine(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Posts>>() {
                    @Override
                    public Observable<Posts> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getGeneralTimeLine(queryBuilder.getGeneralTimeLineParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * gid: If provided, will filter the results to the provided group id.
     * start: Start offset. Use for pagination.
     * unread_only: Set this parameter to 1 to return only unread posts.
     * reverse_order: Set this parameter to 1 to return older posts first.
     *
     * @param queryBuilder
     * @return
     */
    public Observable<Posts> getFeedTimeLine(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Posts>>() {
                    @Override
                    public Observable<Posts> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getFeedTimeLine(queryBuilder.getGeneralTimeLineParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * term: The term which the search will be performed for. only_my_posts: Set this parameter to 1
     * to search only through your own posts. start: Start offset. Use for pagination.
     * reverse_order: Set this parameter to 1 to return older posts first.
     *
     * @param queryBuilder
     * @return
     */
    public Observable<Posts> searchPosts(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Posts>>() {
                    @Override
                    public Observable<Posts> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.searchPosts(queryBuilder.getSearchPostsParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * term: The term which the search will be performed for.
     * start: Start offset. Use for pagination.
     *
     * @param queryBuilder
     * @return
     */
    public Observable<UserSearchInfo> searchUsers(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<UserSearchInfo>>() {
                    @Override
                    public Observable<UserSearchInfo> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.searchUsers(queryBuilder.getSearchUsersParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    /**
     * term: The term which the search will be performed for.
     * start: Start offset. Use for pagination.
     * reverse_order: Set this parameter to 1 to return older posts first.
     *
     * @param queryBuilder
     * @return
     */
    // TODO this is not working due to sit bug
    public Observable<Posts> searchTags(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Posts>>() {
                    @Override
                    public Observable<Posts> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.searchTags(queryBuilder.getSearchTagsParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<AddPost> addPost(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<AddPost>>() {
                    @Override
                    public Observable<AddPost> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.addPost(queryBuilder.getAddPostParams(),
                                queryBuilder.getAddPostParamsPost());
                    }
                });
    }

    public Observable<AddPost> editPost(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<AddPost>>() {
                    @Override
                    public Observable<AddPost> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.editPost(queryBuilder.getEditPostParams(),
                                queryBuilder.getAddPostParamsPost());
                    }
                });
    }

    public Observable<BaseResponse> deletePost(final String pid, final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<BaseResponse>>() {
                    @Override
                    public Observable<BaseResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.deletePost(pid,
                                queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<AddPost> resharePost(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<AddPost>>() {
                    @Override
                    public Observable<AddPost> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.resharePost(queryBuilder.getResharePostParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<AddPost> likePost(final String pid, final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<AddPost>>() {
                    @Override
                    public Observable<AddPost> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.likePost(pid,
                                queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<AddPost> unLikePost(final String pid, final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<AddPost>>() {
                    @Override
                    public Observable<AddPost> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.unLikePost(pid,
                                queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<FollowCount> getMyPostCount(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<FollowCount>>() {
                    @Override
                    public Observable<FollowCount> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getMyPostCount(queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<PostReadsCount> getMyPostReadsCount(final String pid,
                                                          final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<PostReadsCount>>() {
                    @Override
                    public Observable<PostReadsCount> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getMyPostReadsCount(pid, queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<Comment> addComment(final String pid,
                                          final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Comment>>() {
                    @Override
                    public Observable<Comment> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.addComment(pid, queryBuilder.getAddCommentParamsPost());
                    }
                });
    }

    public Observable<BaseResponse> deleteComment(final String cid,
                                          final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<BaseResponse>>() {
                    @Override
                    public Observable<BaseResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.deleteComment(cid, queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<SinglePost> getPost(final String pid,
                                          final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<SinglePost>>() {
                    @Override
                    public Observable<SinglePost> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getPost(pid, queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<Posts> getPostReshares(final String pid,
                                          final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Posts>>() {
                    @Override
                    public Observable<Posts> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getPostReshares(pid, queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<Followers> getPostLikers(final String pid,
                                          final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Followers>>() {
                    @Override
                    public Observable<Followers> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getPostLikers(pid, queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<Like> didLikePost(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Like>>() {
                    @Override
                    public Observable<Like> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.didLikePost(queryBuilder.getDidLikePostParams(),
                                queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<Like> didStarPost(final String pid, final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Like>>() {
                    @Override
                    public Observable<Like> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.didStarPost(pid,
                                queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<Like> starPost(final String pid, final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Like>>() {
                    @Override
                    public Observable<Like> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.starPost(pid, queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<Like> unStarPost(final String pid, final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Like>>() {
                    @Override
                    public Observable<Like> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.unStarPost(pid, queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<PostReadResponse> markPostAsRead(final String pid, final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<PostReadResponse>>() {
                    @Override
                    public Observable<PostReadResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.markPostAsRead(pid, queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<Like> markPostAsUnRead(final String pid, final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<Like>>() {
                    @Override
                    public Observable<Like> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.markPostAsUnRead(pid, queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<NewPostsCount> getNewPostsCount(final String uid, final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<NewPostsCount>>() {
                    @Override
                    public Observable<NewPostsCount> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getNewPostsCount(uid, queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<NotificationList> getNotification(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<NotificationList>>() {
                    @Override
                    public Observable<NotificationList> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.getNotification(queryBuilder.getPostParams());
                    }
                });
    }

    public Observable<PremiumUserResponse> isUserPremium(final QueryBuilder queryBuilder) {
        return getAccessCode(queryBuilder)
                .flatMap(new Func1<String, Observable<PremiumUserResponse>>() {
                    @Override
                    public Observable<PremiumUserResponse> call(String accessCode) {
                        queryBuilder.setAccessCode(accessCode);
                        return mGooderApi.isUserPremium(queryBuilder.getPostParams());
                    }
                });
    }


















    /**
     * If access code is usable return it  otherwise request new one.
     */
    private Observable<String> getAccessCode(QueryBuilder queryBuilder) {
        if (GooderApi.lastOperation - System.currentTimeMillis() > GooderApi.ACCESS_CODE_LIMIT) {
            GooderApi.lastOperation = System.currentTimeMillis();
            return Observable.just(GooderApi.mAccessCode);
        } else {
            return mGooderApi.getAccessCode(queryBuilder.getAccessCodeParams())
                    .flatMap(new Func1<AccessCode, Observable<String>>() {
                        @Override
                        public Observable<String> call(AccessCode accessCode) {
                            GooderApi.mAccessCode = accessCode.getMsgData().getAccessCode();
                            GooderApi.lastOperation = System.currentTimeMillis();
                            return Observable.just(GooderApi.mAccessCode);
                        }
                    });
        }
    }
}
