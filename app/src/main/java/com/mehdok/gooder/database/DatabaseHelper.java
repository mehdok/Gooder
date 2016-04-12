/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mehdok.gooder.network.model.UserInfo;

/**
 * Created by mehdok on 4/10/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static DatabaseHelper mInstance;
    private static final String MAIN_DB_NAME = "mehdok_gooder_db";

    private static final String TBL_USER = "user";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_AVATAR = "avatar";
    private static final String COLUMN_ABOUT = "about";
    private static final String COLUMN_WEB = "web";
    private static final String COLUMN_PASSWORD = "password";

    private final String CREATE_USER_TABLE = "CREATE TABLE " + TBL_USER
            + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_ID + " TEXT NOT NULL UNIQUE, "
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_FULL_NAME + " TEXT, "
            + COLUMN_AVATAR + " TEXT, "
            + COLUMN_ABOUT + " TEXT, "
            + COLUMN_WEB + " TEXT, "
            + COLUMN_PASSWORD + " BLOB);";

    public static DatabaseHelper getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    private DatabaseHelper(Context context)
    {
        super(context, MAIN_DB_NAME, null, 1);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }

    /**
     *
     * @return stored user info in database or null
     */
    public UserInfo getUserInfo()
    {
        String query = "SELECT * FROM " + TBL_USER + ";";
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        if (cursor.moveToFirst())
        {
            UserInfo userInfo = new UserInfo(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_FULL_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ABOUT)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_WEB)),
                    cursor.getBlob(cursor.getColumnIndex(COLUMN_PASSWORD)));

            cursor.close();

            return userInfo;
        }

        return null;
    }

    public boolean putUserInfo(UserInfo userInfo)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userInfo.getUserId());
        values.put(COLUMN_USER_NAME, userInfo.getUsername());
        values.put(COLUMN_FULL_NAME, userInfo.getFullName());
        values.put(COLUMN_AVATAR, userInfo.getAvatar());
        values.put(COLUMN_ABOUT, userInfo.getAbout());
        values.put(COLUMN_WEB, userInfo.getWeb());
        values.put(COLUMN_PASSWORD, userInfo.getPassword());

        long result = getWritableDatabase().insert(TBL_USER, null, values);

        return result != -1;
    }
}
