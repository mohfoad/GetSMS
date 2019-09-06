package com.bme.agricole.smsforwarding;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScheduleSerivce extends Service {
    public static boolean startService = false;
    public int duration = 1000;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("almond", "Schedule Run");
            sendEmptyMessageDelayed(0, duration);
            super.handleMessage(msg);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (startService == false) {
            startService = true;
            mHandler.sendEmptyMessageDelayed(0, duration);
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
