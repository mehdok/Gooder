/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by mehdok on 4/10/2016.
 */
public class util
{
    public static String getDeviceName()
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer))
        {
            return capitalize(model);
        } else
        {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s)
    {
        if (s == null || s.length() == 0)
        {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first))
        {
            return s;
        } else
        {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getSoftwareInfo()
    {
        String softwareInfo = "";
        softwareInfo += "build number: " + Build.DISPLAY;

        return softwareInfo;
    }

    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static String getAppCurrentVersion(Context lContext)
    {
        PackageInfo pInfo = null;
        try
        {
            pInfo = lContext.getPackageManager().getPackageInfo(lContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return "app version exception";
        }

        int cVersion = pInfo.versionCode;
        String nVersion = pInfo.versionName;

        return ("App versionCode: " + cVersion + "\nApp versionName: " + nVersion);
    }
}
