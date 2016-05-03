/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network;

import com.mehdok.gooder.network.exceptions.InvalidUserNamePasswordException;
import com.mehdok.gooder.network.model.Author;
import com.mehdok.gooder.network.model.Extra;
import com.mehdok.gooder.network.model.Flags;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mehdok on 4/11/2016.
 */
public class JsonParser
{
    private final String TAG = "#!tag/";

    private final String MSG_CODE = "msg_code";
    private final String MSG_TYPE = "msg_type";
    private final String MSG_TEXT = "msg_text";
    private final String MSG_DATA = "msg_data";
    private final String ACCESS_CODE = "access_code";
    private final String USER_ID = "uid";
    private final String USERNAME = "username";
    private final String FULLNAME = "fullname";
    private final String AVATAR = "avatar";
    private final String ABOUT = "about";
    private final String WEB = "web";
    private final String PID = "pid";
    private final String AUTHOR = "author";
    private final String UID = "uid";
    private final String TIME = "time";
    private final String PARENT_PID = "parent_pid";
    private final String TITLE = "title";
    private final String POST_BODY = "post_body";
    private final String COMMENTS_COUNT = "comments_count";
    private final String SHARES_COUNT = "shares_count";
    private final String LIKES_COUNT = "likes_count";
    private final String FLAGS = "flags";
    private final String COMMENTS_DISABLED = "comments-disabled";
    private final String RESHARES_DISABLED = "reshares-disabled";
    private final String DRAFTS = "draft";
    private final String EDITED = "edited";
    private final String EXTRA = "extra";
    private final String NOTE = "note";
    private final String URL = "url";

    private SimpleDateFormat dateFormat;
    private Calendar calendar;

    public JsonParser()
    {
        dateFormat = new SimpleDateFormat("yyyy/MM/dd H:m", Locale.US);
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
    }

    public String parseAccessCodeJson(String json) throws InvalidUserNamePasswordException
    {
        if (json != null)
        {
            try
            {
                JSONObject mainJsonObject = new JSONObject(json);

                // if user and password did not match throw an exception
                String msgText = mainJsonObject.getString(MSG_TEXT);
                if (msgText.equalsIgnoreCase("Invalid username or password."))
                    throw new InvalidUserNamePasswordException();

                JSONObject dataObject = mainJsonObject.getJSONObject(MSG_DATA);
                String accessCode = dataObject.getString(ACCESS_CODE);

                return accessCode;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return "";
    }

    /*public UserInfo parseUserInfoJson(String json)
    {
        if (json != null)
        {
            try
            {
                JSONObject mainJsonObject = new JSONObject(json);
                UserInfo userInfo = new UserInfo(mainJsonObject.getString(USER_ID),
                        mainJsonObject.getString(USERNAME),
                        mainJsonObject.getString(FULLNAME),
                        mainJsonObject.getString(AVATAR),
                        mainJsonObject.getString(ABOUT),
                        mainJsonObject.getString(WEB),
                        null);

                return userInfo;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }
*/
    /*public ArrayList<Post> parsePostJson(String json)
    {
        ArrayList<Post> result = new ArrayList<>();

        String pid;
        Author author;
        String time;
        String parentPid;
        String title;
        SpannableString postBody;
        String commentsCount;
        String sharesCount;
        String likesCount;
        Flags flags;
        Extra extra;

        if (json != null)
        {
            try
            {
                JSONObject mainJsonObject = new JSONObject(json);
                JSONArray allPost = mainJsonObject.getJSONArray(MSG_DATA);

                for (int i = 0; i < allPost.length(); i++)
                {
                    JSONObject post = allPost.getJSONObject(i);

                    pid = post.getString(PID);
                    JSONObject authorObj = post.getJSONObject(AUTHOR);
                    author = parseAuthor(authorObj);
                    time = post.getString(TIME);
                    parentPid = post.getString(PARENT_PID);
                    title = post.getString(TITLE);
                    postBody = PrettySpann.getPrettyString(post.getString(POST_BODY), TAG, null);//TODO click listener
                    commentsCount = post.getString(COMMENTS_COUNT);
                    sharesCount = post.getString(SHARES_COUNT);
                    likesCount = post.getString(LIKES_COUNT);
                    JSONObject flagObj = post.getJSONObject(FLAGS);
                    flags = parseFlag(flagObj);
                    JSONObject extraObj = post.getJSONObject(EXTRA);
                    extra = parseExtra(extraObj);

                    result.add(new Post(pid,
                            author,
                            getReadableDate(time),
                            parentPid,
                            title,
                            postBody,
                            commentsCount,
                            sharesCount,
                            likesCount,
                            flags,
                            extra));
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return result;
    }*/

    private Author parseAuthor(JSONObject obj) throws JSONException
    {
        return new Author(obj.getString(UID),
                obj.getString(FULLNAME));
    }

    private Flags parseFlag(JSONObject obj)throws JSONException
    {
        return new Flags(obj.getBoolean(COMMENTS_DISABLED),
                obj.getBoolean(RESHARES_DISABLED),
                obj.getBoolean(DRAFTS),
                obj.getBoolean(EDITED));
    }

    private Extra parseExtra(JSONObject obj)throws JSONException
    {
        return new Extra(obj.getString(NOTE),
                obj.getString(URL));
    }

    private String getReadableDate(String date)
    {
        long time = Long.parseLong(date);
        calendar.setTimeInMillis(time * 1000);
        return dateFormat.format(calendar.getTime());
    }

    private String replaceForumUrl(String str)
    {
        int openTagIndex = str.indexOf("[url=");
        if (openTagIndex != -1)
        {
            str = str.subSequence(0, openTagIndex) + "<a href=\"" + str.substring(openTagIndex + 5, str.length());
            int secondOpenTagIndex = str.indexOf("]", openTagIndex);
            str = str.subSequence(0, secondOpenTagIndex) + "\">" + str.substring(secondOpenTagIndex +1, str.length());
            str = str.replace("[/url]", "</a>");
        }

        return str;
    }
}
