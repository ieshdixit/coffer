package elope.crazyones.com.coffer.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import elope.crazyones.com.coffer.model.ReceivedSMS;
import elope.crazyones.com.coffer.serverapi.ApiClient;
import elope.crazyones.com.coffer.serverapi.interfaces.receiveSMSAPI;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    String msg, sender = "";
    private Listener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent received:"+intent.getAction());

        if(intent.getAction().equals(SMS_RECEIVED)){
            Bundle databundle = intent.getExtras();
            if(databundle!=null){
                Object[] mpdu = (Object[]) databundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mpdu.length];
                for(int i=0;i<mpdu.length;i++){
                    String format = databundle.getString("format");
                    message[i] =SmsMessage.createFromPdu((byte[])mpdu[i],format);
                    msg = message[i].getMessageBody();
                    sender = message[i].getOriginatingAddress();
                }
                Toast.makeText(context, msg+" "+sender, Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onTextReceived(String text);
    }
}
