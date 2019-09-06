package com.bme.agricole.smsforwarding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsController extends BroadcastReceiver {
    private Context mContext;

    public SmsController() {
        super();
    }

    public void onReceive(Context context, Intent intent) {
        this.mContext = context;

        try{
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from = null;

            if (bundle != null){
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus != null) {
                        msgs = new SmsMessage[pdus.length];
                        for (int k = 0; k < msgs.length; k++) {
                            msgs[k] = SmsMessage.createFromPdu((byte[]) pdus[k]);
                            msg_from = msgs[k].getOriginatingAddress();
                            String msgBody = msgs[k].getMessageBody();
                            long timestamp = msgs[k].getTimestampMillis();

                            Log.e("Received From ::::::", msg_from);
                            Log.e("Received Message ::::::", msgBody);
                            Log.e("Received Date ::::::", Utils.getDate(timestamp));
                        }
                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
