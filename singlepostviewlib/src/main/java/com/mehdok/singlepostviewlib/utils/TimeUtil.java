/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mehdok on 5/2/2016.
 */
public class TimeUtil {
    private SimpleDateFormat dateFormat;
    private Calendar calendar;
    private static TimeUtil mInstance;

    public static TimeUtil getInstance() {
        if (mInstance == null) {
            mInstance = new TimeUtil();
        }
        return mInstance;
    }

    private TimeUtil() {
        dateFormat = new SimpleDateFormat("yyyy/MM/dd H:m", Locale.US);
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
    }

    public String getReadableDate(String date) {
        long time = Long.parseLong(date);
        calendar.setTimeInMillis(time * 1000);
        return dateFormat.format(calendar.getTime());
    }

    public String getNow() {
        calendar.setTime(new Date());
        return dateFormat.format(calendar.getTime());
    }
}
