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
 * @author mehdok on 5/2/2016.
 * <p>Any time format happens here</p>
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
        if (date == null || date.isEmpty()) return "";

        long time = Long.parseLong(date);
        calendar.setTimeInMillis(time * 1000);

        Calendar now = Calendar.getInstance();
        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
        if (now.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
            return "Today " + DateFormat.format(timeFormatString, calendar);
        } else if (now.get(Calendar.DATE) - calendar.get(Calendar.DATE) == 1) {
            return "Yesterday " + DateFormat.format(timeFormatString, calendar);
        } else if (now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, calendar).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy, h:mm aa", calendar).toString();
        }


        //        return dateFormat.format(calendar.getTime());
    }

    public String getNow() {
        calendar.setTime(new Date());
        return dateFormat.format(calendar.getTime());
    }
}
