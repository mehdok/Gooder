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

    // POST PARAMS
    private static final String KEY_CLIENT_ID = "client_id";
    private static final String KEY_ACCESS_CODE = "access_code";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_PASSWORD = "password";
    private String accessCode;
    private String userName;    //Required
    private String password;    //Required

    // GET PARAMS
    private static final String KEY_GID = "gid";
    private static final String KEY_START = "start";
    private static final String KEY_UNREAD_ONLY = "unread_only";
    private static final String KEY_REVERSE_ORDER = "reverse_order";
    private String gid;
    private String start;
    private String unreadOnly;
    private String reverseOrder;

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

    public void setStart(String start) {
        this.start = start;
    }

    public void setUnreadOnly(String unreadOnly) {
        this.unreadOnly = unreadOnly;
    }

    public void setReverseOrder(String reverseOrder) {
        this.reverseOrder = reverseOrder;
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
     *
     * @return a Map of params for Get query, like <b>&uid=5&gid=8</b>
     */
    public Map<String, String> getGetParams() {
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
}
