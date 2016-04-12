/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by mehdok on 4/10/2016.
 */
public class Util
{
    private static final String SUPPORT_EMAIL = "mehdok@ymail.com";
    private static String appVersionName;
    public static final String LOG_DIR = "logs";
    public static final String LOG_ZIP_DIR = "log_zip";

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

    public static String getAppCurrentVersion(Context lContext)
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

    public static void sendBugReport(Context ctx, String reportTitle, String reportSummery)
    {
        File logDir = new File(ctx.getFilesDir(), LOG_DIR);
        logDir.mkdirs();
        File logZipDir = new File(ctx.getFilesDir(), LOG_ZIP_DIR);
        logZipDir.mkdirs();
        String ZIP_LOG = logZipDir.getAbsolutePath() + "/log.zip";

        extractLog("end - no crash", ctx, logDir.getAbsolutePath());
        boolean zipResult = zipFileAtPath(logDir.getAbsolutePath(), ZIP_LOG);
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{SUPPORT_EMAIL});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, reportTitle + " -- version: " + getAppVersionName(ctx));
        emailIntent.putExtra(Intent.EXTRA_TEXT, reportSummery);
        if (zipResult)
        {
            File zipFile = new File(ZIP_LOG);
            Uri contentUri = FileProvider.getUriForFile(ctx, "com.mehdok.gooder.fileprovider", zipFile);
            emailIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        }
        ctx.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public static File extractLog(String trace, Context context, String logDir)
    {
        Date datum = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
        String fullName = df.format(datum) + "-gooder-log-report.log";
        File file = new File(logDir, fullName);

        //clears a file
        if (file.exists())
        {
            file.delete();
        }

        //write log to file
        int pid = android.os.Process.myPid();
        try
        {
            String command = String.format("logcat -d -v threadtime *:*");
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();

            //get device info
            result.append(getDeviceName());
            result.append("\n");
            result.append(getSoftwareInfo());
            result.append("\n");
            result.append(getAppCurrentVersion(context));
            result.append("\n");
            result.append("**********\n\n");

            String currentLine = null;

            while ((currentLine = reader.readLine()) != null)
            {
                if (currentLine != null && currentLine.contains(String.valueOf(pid)))
                {
                    result.append(currentLine);
                    result.append("\n");
                }
            }

            result.append(trace);

            FileWriter out = new FileWriter(file);
            out.write(result.toString());
            out.close();
        } catch (IOException e)
        {
            Log.e("extractLog", e.getMessage());
        }

        //clear the log
        try
        {
            Runtime.getRuntime().exec("logcat -c");
        } catch (IOException e)
        {
            Log.e("extractLog", e.getMessage());
        }

        return file;
    }

    public static String getAppVersionName(Context context)
    {
        if (appVersionName == null)
        {
            PackageInfo pInfo = null;
            try
            {
                pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                appVersionName = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e)
            {
                e.printStackTrace();
                appVersionName = "0";
            }
        }

        return appVersionName;
    }

    public static boolean zipFileAtPath(String sourcePath, String toLocation)
    {
        final int BUFFER = 2048;

        File sourceFile = new File(sourcePath);
        try
        {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            if (sourceFile.isDirectory())
            {
                zipSubFolder(out, sourceFile, sourceFile.getParent().length());
            } else
            {
                byte data[] = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(sourcePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1)
                {
                    out.write(data, 0, count);
                }
            }
            out.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void zipSubFolder(ZipOutputStream out, File folder, int basePathLength) throws IOException
    {
        final int BUFFER = 2048;

        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        for (File file : fileList)
        {
            if (file.isDirectory())
            {
                zipSubFolder(out, file, basePathLength);
            } else
            {
                byte data[] = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath
                        .substring(basePathLength);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1)
                {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
    }

    private static String getLastPathComponent(String filePath)
    {
        String[] segments = filePath.split("/");
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }

}
