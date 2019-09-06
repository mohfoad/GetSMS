package com.bme.agricole.smsforwarding;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    public static final String INBOX = "content://sms/inbox";
    public static final String DRAFT = "content://sms/draft";
    private static final String TAG = "MainActivity";
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.verifyStoragePermissions(this);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            /* monitor outgoing messages with content observer */
            ContentObserver co = new SMSoutObserver(new Handler(), getApplicationContext());
            ContentResolver contentResolver = getApplicationContext().getContentResolver();
            contentResolver.registerContentObserver(Uri.parse(Utils.SMSBOX),true, co);
            Utils.dbBackupSync(getBaseContext().getContentResolver());
            /* end */
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("almond", "Destroy MainActivity");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
                /* monitor outgoing messages with content observer */
                ContentObserver co = new SMSoutObserver(new Handler(), getApplicationContext());
                ContentResolver contentResolver = getApplicationContext().getContentResolver();
                contentResolver.registerContentObserver(Uri.parse(Utils.SMSBOX),true, co);
                Utils.dbBackupSync(getBaseContext().getContentResolver());
                /* end */
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public List<Sms> getAllSms() {
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = getBaseContext().getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        MainActivity.this.startManagingCursor(c);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                Log.e("" + (i + 1) + "st Message", "*******************************************");
                Log.e("------Address-----", c.getString(c.getColumnIndexOrThrow("address")));
                Log.e("------Body-----", c.getString(c.getColumnIndexOrThrow("body")));
                Log.e("------Read-----", c.getString(c.getColumnIndexOrThrow("read")));
                Log.e("------Date-----", c.getString(c.getColumnIndexOrThrow("date")));
                Log.e("------Type-----", c.getString(c.getColumnIndexOrThrow("type")));
                Log.e("#################", "########################################");
                lstSms.add(objSms);
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();

        return lstSms;
    }

    public class Sms{
        private String _id;
        private String _address;
        private String _msg;
        private String _readState; //"0" for have not read sms and "1" for have read sms
        private String _time;
        private String _folderName;

        public String getId(){
            return _id;
        }
        public String getAddress(){
            return _address;
        }
        public String getMsg(){
            return _msg;
        }
        public String getReadState(){
            return _readState;
        }
        public String getTime(){
            return _time;
        }
        public String getFolderName(){
            return _folderName;
        }


        public void setId(String id){
            _id = id;
        }
        public void setAddress(String address){
            _address = address;
        }
        public void setMsg(String msg){
            _msg = msg;
        }
        public void setReadState(String readState){
            _readState = readState;
        }
        public void setTime(String time){
            _time = time;
        }
        public void setFolderName(String folderName){
            _folderName = folderName;
        }

    }
}
