package com.bme.agricole.smsforwarding;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startBackgroundService();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startBackgroundService() {
        if (ScheduleSerivce.startService == false) {
            startService(new Intent(this, ScheduleSerivce.class));
        }
    }
}