package com.bme.agricole.smsforwarding;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    public static final String SMSBOX = "content://sms";
    public static String getDate(long time) {
        Date date = new Date(time*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy "); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));

        return sdf.format(date);
    }

    public static void dbBackupSync(ContentResolver cr) {
        String msgs = readSystemSMS(cr);
        generateBMEDOnSD("backup.db", msgs);
    }

    public static String readSystemSMS(ContentResolver cr) {
        StringBuilder wholeMsgData = new StringBuilder();
        Cursor cursor = cr.query(Uri.parse(SMSBOX), null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String str = " " + cursor.getColumnName(i) + ":" + cursor.getString(i);
                    wholeMsgData.append(str);
                }
                wholeMsgData.append('\n');
            } while (cursor.moveToNext());
            cursor.close();
        }

        return wholeMsgData.toString();
    }

    public static void generateBMEDOnSD(String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory() ,"BMED");
            Boolean created = true;
            if (!root.exists()) {
                created = root.mkdirs();
            }
            File newFile = new File(root, "backup.txt");

            created = newFile.createNewFile();
            if (created) {
                FileWriter writer = new FileWriter(newFile);
                writer.append(sBody);
                writer.flush();
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
