/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mehdok on 5/1/2016.
 */
public class QueryBuilder {

    public enum Value {
        YES("1"),
        NO("0");

        private String mValue;
        private Value(String value) {
            mValue = value;
        }
        @Override
        public String toString() {
            return mValue;
        }
    }

    public enum Type {
        LIKES("likes"),
        SHARES("shares"),
        COMMENTS("comments");

        private String mValue;
        private Type(String value) {
            mValue = value;
        }
        @Override
        public String toString() {
            return mValue;
        }
    }

    public enum TypeFactor {
        ME("me"),
        ME_FRIENDS("me-friends"),
        MY_POSTS("my-posts");

        private String mValue;
        private TypeFactor(String value) {
            mValue = value;
        }
        @Override
        public String toString() {
            return mValue;
        }
    }

    // POST PARAMS
    private static final String KEY_CLIENT_ID = "client_id";
    private static final String KEY_ACCESS_CODE = "access_code";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_PASSWORD = "password";
    private String accessCode;
    private String userName;    //Required
    private String password;    //Required

    // GET PARAMS
    private static final String KEY_UID = "uid";
    private static final String KEY_GID = "gid";
    private static final String KEY_START = "start";
    private static final String KEY_UNREAD_ONLY = "unread_only";
    private static final String KEY_REVERSE_ORDER = "reverse_order";
    private static final String KEY_TYPE = "type";
    private static final String KEY_ONLY_MY_POSTS = "only_my_posts";
    private static final String KEY_TERM = "term";
    private static final String KEY_DISABLE_COMMENTS = "disable_comments";
    private static final String KEY_DISABLE_RESHARES = "disable_reshares";
    private static final String KEY_DRAFT = "draft";
    private static final String KEY_POST_TITLE = "post_title";
    private static final String KEY_POST_BODY = "post_body";
    private static final String KEY_PID = "pid";
    private static final String KEY_NOTE_BODY = "note_body";
    private static final String KEY_COMMENT_BODY = "comment_body";
    private String uid;
    private String gid;
    private String start;
    private String unreadOnly;
    private String reverseOrder;
    private String type;
    private String typeFactor;
    private String onlyMyPosts;
    private String term;
    private String disableComments;
    private String disableReshares;
    private String draft;
    private String postTitle;
    private String postBody;
    private String pid;
    private String noteBody;
    private String commentBody;

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setStart(int start) {
        this.start = String.valueOf(start);
    }

    public void setUnreadOnly(Value unreadOnly) {
        this.unreadOnly = unreadOnly.toString();
    }

    public void setReverseOrder(Value reverseOrder) {
        this.reverseOrder = reverseOrder.toString();
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setType(Type type) {
        this.type = type.toString();
    }

    public void setTypeFactor(TypeFactor typeFactor) {
        this.typeFactor = typeFactor.toString();
    }

    public void setOnlyMyPosts(Value onlyMyPosts) {
        this.onlyMyPosts = onlyMyPosts.toString();
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setDisableComments(String disableComments) {
        this.disableComments = disableComments;
    }

    public void setDisableReshares(String disableReshares) {
        this.disableReshares = disableReshares;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public void setNoteBody(String noteBody) {
        this.noteBody = noteBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    /**
     *
     * @return a Map based on client id, username and password
     *          This is just usable for getting access_code
     */
    public Map<String, String> getAccessCodeParams() {
        Map<String, String> query = new HashMap<>();
        query.put(KEY_CLIENT_ID, GooderApi.apiKey);

        if (userName == null || password == null)
            throw new RuntimeException("user name and password is not set");

        query.put(KEY_USER_NAME, userName);
        query.put(KEY_PASSWORD, password);

        return query;
    }

    /**
     *
     * @return a Map of params for post query
     */
    public Map<String, String> getPostParams() {
        Map<String, String> query = new HashMap<>();
        query.put(KEY_CLIENT_ID, GooderApi.apiKey);

        if (accessCode !=null)
            query.put(KEY_ACCESS_CODE, accessCode);

        return query;
    }

    /**
     * uid: if not provided, the authenticated user's id will be used.
     *
     * @return
     */
    public Map<String, String> getUserInfoParams() {
        Map<String, String> query = new HashMap<>();
        if (uid != null)
            query.put(KEY_UID, uid);
        return query;
    }

    /**
     * gid: If provided, only members of the provided group will be returned.
     *
     * @return
     */
    public Map<String, String> getFollowedUsersParams() {
        Map<String, String> query = new HashMap<>();
        if (gid != null)
            query.put(KEY_GID, gid);
        return query;
    }

    /**
     * gid: If provided, only members of the provided group will be returned.
     *
     * @return
     */
    public Map<String, String> getFollowedFeedParams() {
        Map<String, String> query = new HashMap<>();
        if (gid != null)
            query.put(KEY_GID, gid);
        return query;
    }

    /**
     * gid: If provided, only members of the provided group will be returned.
     *
     * @return
     */
    public Map<String, String> getFollowedUserAndFeedParams() {
        Map<String, String> query = new HashMap<>();
        if (gid != null)
            query.put(KEY_GID, gid);
        return query;
    }

    /**
     * start: Start offset. Use for pagination.
     * unread_only: Set this parameter to 1 to return only unread posts.
     * reverse_order: Set this parameter to 1 to return older posts first.
     *
     * @return
     */
    public Map<String, String> getUserPostsParams() {
        Map<String, String> query = new HashMap<>();
        if (uid != null)
            query.put(KEY_UID, uid);
        if (start != null)
            query.put(KEY_START, start);
        if (unreadOnly != null)
            query.put(KEY_UNREAD_ONLY, unreadOnly);
        if (reverseOrder != null)
            query.put(KEY_REVERSE_ORDER, reverseOrder);
        return query;
    }

    /**
     * type: The factor which the recommendation will be based on. Can be one of likes, shares,
     * or comments. If not provided, likes will be used.
     * start: Start offset. Use for pagination.
     *
     * @return
     */
    public Map<String, String> getRecommendedPostsParams() {
        Map<String, String> query = new HashMap<>();
        if (start != null)
            query.put(KEY_START, start);
        if (type != null)
            query.put(KEY_TYPE, type);
        return query;
    }

    /**
     * start: Start offset. Use for pagination.
     *
     * @return
     */
    public Map<String, String> getRandomPostsParams() {
        Map<String, String> query = new HashMap<>();
        if (start != null)
            query.put(KEY_START, start);
        return query;
    }

    /**
     * type: The factor to filter the commented posts based on. Can be one of me, me-friends,
     * or my-posts. If not provided, me will be used.
     * start: Start offset. Use for pagination.
     *
     * @return
     */
    public Map<String, String> getCommentedPostsParams() {
        Map<String, String> query = new HashMap<>();
        if (start != null)
            query.put(KEY_START, start);
        if (typeFactor != null)
            query.put(KEY_TYPE, typeFactor);
        return query;
    }

    /**
     * start: Start offset. Use for pagination.
     *
     * @return
     */
    public Map<String, String> getStaredItemParams() {
        Map<String, String> query = new HashMap<>();
        if (start != null)
            query.put(KEY_START, start);
        return query;
    }

    /**
     * gid: If provided, will filter the results to the provided group id.
     * start: Start offset. Use for pagination.
     * unread_only: Set this parameter to 1 to return only unread posts.
     * reverse_order: Set this parameter to 1 to return older posts first.
     *
     * @return
     */
    public Map<String, String> getGeneralTimeLineParams() {
        Map<String, String> query = new HashMap<>();
        if (gid != null)
            query.put(KEY_GID, gid);
        if (start != null)
            query.put(KEY_START, start);
        if (unreadOnly != null)
            query.put(KEY_UNREAD_ONLY, unreadOnly);
        if (reverseOrder != null)
            query.put(KEY_REVERSE_ORDER, reverseOrder);
        return query;
    }

    /**
     * gid: If provided, will filter the results to the provided group id.
     * start: Start offset. Use for pagination.
     * unread_only: Set this parameter to 1 to return only unread posts.
     * reverse_order: Set this parameter to 1 to return older posts first.
     *
     * @return
     */
    public Map<String, String> getFeedsTimeLineParams() {
        Map<String, String> query = new HashMap<>();
        if (gid != null)
            query.put(KEY_GID, gid);
        if (start != null)
            query.put(KEY_START, start);
        if (unreadOnly != null)
            query.put(KEY_UNREAD_ONLY, unreadOnly);
        if (reverseOrder != null)
            query.put(KEY_REVERSE_ORDER, reverseOrder);
        return query;
    }

    /**
     * gid: If provided, will filter the results to the provided group id.
     * start: Start offset. Use for pagination.
     * unread_only: Set this parameter to 1 to return only unread posts.
     * reverse_order: Set this parameter to 1 to return older posts first.
     *
     * @return
     */
    public Map<String, String> getUsersTimeLineParams() {
        Map<String, String> query = new HashMap<>();
        if (gid != null)
            query.put(KEY_GID, gid);
        if (start != null)
            query.put(KEY_START, start);
        if (unreadOnly != null)
            query.put(KEY_UNREAD_ONLY, unreadOnly);
        if (reverseOrder != null)
            query.put(KEY_REVERSE_ORDER, reverseOrder);
        return query;
    }

    /**
     * term: The term which the search will be performed for.
     * only_my_posts: Set this parameter to 1 to search only through your own posts.
     * start: Start offset. Use for pagination.
     * reverse_order: Set this parameter to 1 to return older posts first.
     *
     * @return
     */
    public Map<String, String> getSearchPostsParams() {
        Map<String, String> query = new HashMap<>();
        if (term == null)
            throw new RuntimeException("term is required");

        query.put(KEY_TERM, term);
        if (start != null)
            query.put(KEY_START, start);
        if (onlyMyPosts != null)
            query.put(KEY_ONLY_MY_POSTS, onlyMyPosts);
        if (reverseOrder != null)
            query.put(KEY_REVERSE_ORDER, reverseOrder);
        return query;
    }

    /**
     * term: The term which the search will be performed for.
     * start: Start offset. Use for pagination.
     *
     * @return
     */
    public Map<String, String> getSearchUsersParams() {
        Map<String, String> query = new HashMap<>();
        if (term == null)
            throw new RuntimeException("term is required");

        query.put(KEY_TERM, term);
        if (start != null)
            query.put(KEY_START, start);
        return query;
    }

    /**
     * term: The term which the search will be performed for.
     * start: Start offset. Use for pagination.
     * reverse_order: Set this parameter to 1 to return older posts first.
     *
     * @return
     */
    public Map<String, String> getSearchTagsParams() {
        Map<String, String> query = new HashMap<>();
        if (term == null)
            throw new RuntimeException("term is required");

        query.put(KEY_TERM, term);
        if (start != null)
            query.put(KEY_START, start);
        if (reverseOrder != null)
            query.put(KEY_REVERSE_ORDER, reverseOrder);
        return query;
    }

    /**
     * disable_comments: Set this parameter to 1 to disable comments for the post.
     * disable_reshares: Set this parameter to 1 to disable resharing for the post.
     * draft: Set this parameter to 1 to save the post as draft (not published).
     *
     * @return
     */
    public Map<String, String> getAddPostParams() {
        Map<String, String> query = new HashMap<>();
        if (disableComments != null)
            query.put(KEY_DISABLE_COMMENTS, disableComments);
        if (disableReshares != null)
            query.put(KEY_DISABLE_RESHARES, disableReshares);
        if (draft != null)
            query.put(KEY_DRAFT, draft);
        return query;
    }

    /**
     * Also used for editing post
     *
     * @return
     */
    public Map<String, String> getAddPostParamsPost() {
        Map<String, String> query = new HashMap<>();

        if (postBody == null || postTitle == null)
            throw new RuntimeException("postBody and postTitle is required");

        query.put(KEY_POST_TITLE, postTitle);
        query.put(KEY_POST_BODY, postBody);

        return query;
    }

    /**
     * pid: The id of the post which needs get edited.
     * disable_comments: Set this parameter to 1 to disable comments for the post.
     * disable_reshares: Set this parameter to 1 to disable resharing for the post.
     * draft: Set this parameter to 1 to save the post as draft (not published).
     *
     * @return
     */
    public Map<String, String> getEditPostParams() {
        Map<String, String> query = new HashMap<>();

        if (pid == null)
            throw new RuntimeException("pid is required");

        query.put(KEY_PID, pid);
        if (disableComments != null)
            query.put(KEY_DISABLE_COMMENTS, disableComments);
        if (disableReshares != null)
            query.put(KEY_DISABLE_RESHARES, disableReshares);
        if (draft != null)
            query.put(KEY_DRAFT, draft);
        return query;
    }

    /**
     * note_body: The optional note for the reshared post.
     *
     * @return
     */
    public Map<String, String> getResharePostParams() {
        Map<String, String> query = new HashMap<>();

        if (pid == null)
            throw new RuntimeException("pid is required");

        query.put(KEY_PID, pid);
        if (noteBody != null)
            query.put(KEY_NOTE_BODY, noteBody);
        return query;
    }

    public Map<String, String> getAddCommentParamsPost() {
        Map<String, String> query = new HashMap<>();

        if (commentBody == null)
            throw new RuntimeException("commentBody is required");

        query.put(KEY_COMMENT_BODY, commentBody);
        return query;
    }

    public Map<String, String> getDidLikePostParams() {
        Map<String, String> query = new HashMap<>();

        if (pid == null)
            throw new RuntimeException("pid is required");

        query.put(KEY_PID, pid);
        if (uid != null)
            query.put(KEY_UID, uid);
        return query;
    }

    public Map<String, String> getMarkUserAsReadParams() {
        Map<String, String> query = new HashMap<>();

        if (uid == null)
            throw new RuntimeException("uid is required");

        query.put(KEY_UID, uid);
        if (pid != null)
            query.put(KEY_PID, pid);
        return query;
    }
}
