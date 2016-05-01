/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mehdok.gooder.R;
import com.mehdok.gooder.utils.CustomExceptionHandler;
import com.mehdok.gooder.utils.Util;
import com.mehdok.gooder.views.VazirButton;

import java.io.File;

/**
 * Created by Mehdi Sohrabi (mehdok@gmail.com) on 1/14/2015.
 */
public class CrashReporterActivity extends AppCompatActivity {
    String crashLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crash_reporter);

        crashLocation = getIntent().getExtras().getString(CustomExceptionHandler.CRASH_TAG);

        VazirButton button = (VazirButton) findViewById(R.id.sendReportButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCrashReport();
            }
        });
    }

    public void sendCrashReport() {
        File logZipDir = new File(this.getFilesDir(), Util.LOG_ZIP_DIR);
        logZipDir.mkdirs();
        String ZIP_LOG = logZipDir.getAbsolutePath() + "/log.zip";

        boolean zipResult = Util.zipFileAtPath(crashLocation, ZIP_LOG);
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {Util.SUPPORT_EMAIL});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crash_email_subject) +
                " -- version: " +
                Util.getAppVersionName(this));
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.crash_email_context));
        if (zipResult) {
            File zipFile = new File(ZIP_LOG);
            Uri contentUri =
                    FileProvider.getUriForFile(this, "com.mehdok.gooder.fileprovider", zipFile);
            emailIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        }
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));

        this.finish();
    }
}
