/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.singlepostviewlib.utils;

import android.text.format.DateFormat;

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
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(time * 1000);
        calendar.setTimeInMillis(time * 1000);

        Calendar now = Calendar.getInstance();
        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "Today " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString();
        }


        //        return dateFormat.format(calendar.getTime());
    }

    public String getNow() {
        calendar.setTime(new Date());
        return dateFormat.format(calendar.getTime());
    }
}
