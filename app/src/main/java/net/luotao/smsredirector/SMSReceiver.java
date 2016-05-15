package net.luotao.smsredirector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import android.telephony.gsm.SmsManager;
import android.os.Bundle;

public class SMSReceiver extends BroadcastReceiver {
    public static final String TAG = "ImiChatSMSReceiver";
    //android.provider.Telephony.Sms.Intents
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
//            SmsMessage[] messages = getMessagesFromIntent(intent);
//            for (SmsMessage message : messages) {
//
//                CharSequence text = "Hello toast!";
//                int duration = Toast.LENGTH_LONG;
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();
//
//                Log.i(TAG, message.getOriginatingAddress() + " : " +
//                        message.getDisplayOriginatingAddress() + " : " +
//                        message.getDisplayMessageBody() + " : " +
//                        message.getTimestampMillis());
//            }
//        }
        if (SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            StringBuilder sb = new StringBuilder();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] msg = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                for (SmsMessage curMsg : msg) {
                    sb.append("You got the message From:【");
                    sb.append(curMsg.getDisplayOriginatingAddress());
                    sb.append("】Content：");
                    sb.append(curMsg.getDisplayMessageBody());
                }
                Toast.makeText(context,
                        "Got The Message:" + sb.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public final SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }

        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];

        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }
}