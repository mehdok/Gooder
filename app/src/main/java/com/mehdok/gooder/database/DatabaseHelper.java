/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mehdok on 4/10/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static DatabaseHelper mInstance;
    private static final String MAIN_DB_NAME = "mehdok_gooder_db";


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
        // TODO init tables
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
