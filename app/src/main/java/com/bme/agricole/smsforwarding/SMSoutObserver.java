package com.bme.agricole.smsforwarding;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SMSoutObserver extends ContentObserver {
    private static final Uri uri = Uri.parse("content://sms");
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_BODY = "body";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_DATE = "date";
    private static final int MESSAGE_TYPE_SENT = 2;

    private Context mCtx;

    public SMSoutObserver(Handler handler, Context ctx) {
        super(handler);
        mCtx = ctx;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Cursor cursor = null;

        try {
            cursor = mCtx.getContentResolver().query(uri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
                String body = cursor.getString(cursor.getColumnIndex(COLUMN_BODY));
                String address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS));
                int timestamp = cursor.getInt(cursor.getColumnIndex(COLUMN_DATE));

                if (type == MESSAGE_TYPE_SENT) {
                    // Sent message
                    Log.e("Sent From ::::::", address);
                    Log.e("Sent Message ::::::", body);
                    Log.e("Sent Date ::::::", Utils.getDate(timestamp));
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }
}
