/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomExceptionHandler implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler defaultUEH;

    private static WeakReference<Context> lcontext;
    private static String appName;
    private Intent mEmailIntent;

    public static final String CRASH_TAG = "crash";

    /* 
     * if any of the parameters is null, the respective functionality 
     * will not be used 
     */
    public CustomExceptionHandler(Context context, String appname, Intent emailIntent) {
        lcontext = new WeakReference<Context>(context);
        this.appName = appname;
        this.mEmailIntent = emailIntent;
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread t, Throwable e) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();

        Date datum = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
        String fullName = df.format(datum) + "-" + appName + "-crash-report.log";

        File logDir = new File(lcontext.get().getFilesDir(), Util.LOG_DIR);
        logDir.mkdirs();

        extractLog(stacktrace, logDir.getAbsolutePath(), fullName);

        restartApp(logDir.getAbsolutePath() + "/" + fullName);
        //defaultUEH.uncaughtException(t, e);
    }

    public static File extractLog(String trace, String logDir, String fullName) {
        File file = new File(logDir, fullName);

        //clears a file
        if (file.exists()) {
            file.delete();
        }

        //write log to file
        int pid = android.os.Process.myPid();
        try {
            String command = String.format("logcat -d -v threadtime *:*");
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();

            //get device info
            result.append(Util.getDeviceName());
            result.append("\n");
            result.append(Util.getSoftwareInfo());
            result.append("\n");
            result.append(Util.getAppCurrentVersion(lcontext.get()));
            result.append("\n");
            result.append("**********\n\n");

            String currentLine = null;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine != null && currentLine.contains(String.valueOf(pid))) {
                    result.append(currentLine);
                    result.append("\n");
                }
            }

            result.append(trace);

            FileWriter out = new FileWriter(file);
            out.write(result.toString());
            out.close();

        } catch (IOException e) {
        }

        //clear the log
        try {
            Runtime.getRuntime().exec("logcat -c");
        } catch (IOException e) {
        }

        return file;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void restartApp(String crashAddress) {
        Logger.t("CustomExceptionHandler").e("restartApp");

        mEmailIntent.putExtra(CRASH_TAG, crashAddress);
        lcontext.get().startActivity(mEmailIntent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}

