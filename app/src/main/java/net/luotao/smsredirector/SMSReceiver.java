package net.luotao.smsredirector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.telstra.api.http.client.APICallBack;
import com.telstra.api.http.client.HttpContext;
import java.util.Calendar;
import java.util.Hashtable;

import com.telstra.api.models.AuthenticationResponse;
import com.telstra.api.models.GetAuthenticationInput;
import com.telstra.api.models.SendMessageRequest;
import com.telstra.api.models.SendMessageResponse;
import com.telstra.api.Configuration;
import com.telstra.api.controllers.APIController;

public class SMSReceiver extends BroadcastReceiver {
    public static final String TAG = "ImiChatSMSReceiver";
    //android.provider.Telephony.Sms.Intents
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    private static final String ENABLE_KEY = "enable_switch";
    private static final String MOBILE_NUMBER_KEY = "receive_number_text";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (!this.getIsEnabled(context)) {
            Log.i("onReceive()", "Enable = false, skipped.");
            return;
        }

        if (SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            Hashtable<String, String> msg_table = new Hashtable<String, String>();

            StringBuilder sb = new StringBuilder();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] msg = new SmsMessage[pdus.length];

                for (int i = 0; i < pdus.length; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = bundle.getString("format");
                        msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else {
                        msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    String sender = msg[i].getDisplayOriginatingAddress();
                    String body = msg[i].getDisplayMessageBody();//msg[i].getMessageBody();

                    if (msg_table.containsKey(sender)) {
                        msg_table.put(sender, msg_table.get(sender) + body);
                    } else {
                        msg_table.put(sender, body);
                    }
                }
            }

            for (String sender : msg_table.keySet()) {
                String sms_body = "[" + sender + "]" + msg_table.get(sender);
                this.checkTokenAndSendSMS(context, sms_body);
            }

//                Toast.makeText(context,
//                        String.valueOf(msg.length)+" Got The Message:" + sb.toString(),
//                        Toast.LENGTH_LONG).show();
        }

    }

    private String getMobileNumber(Context context){
        String mobileNumber = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(MOBILE_NUMBER_KEY, "");
        return  mobileNumber;
    }

    private Boolean getIsEnabled(Context context){
        Boolean isEnabled = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(ENABLE_KEY, false);
        return isEnabled;
    }

    private void checkTokenAndSendSMS(final Context context, final String body){
        Configuration.initialize(context);
        Date now = new Date();
        if (now.before(Configuration.tokenExpiry)) {
            this.sendSMS(context,Configuration.selfMobileNumber,body);
        }else{
            GetAuthenticationInput authRequest = new GetAuthenticationInput();
            authRequest.setClientId(Configuration.consumerKey);
            authRequest.setClientSecret(Configuration.consumerSecret);

            APIController controller = new APIController();
            controller.getAuthenticationAsync(authRequest, new APICallBack<AuthenticationResponse>() {
                public void onSuccess(HttpContext httpcontext, AuthenticationResponse response) {
                    Configuration.oAuthAccessToken = response.getAccessToken();

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.SECOND, Integer.parseInt(response.getExpiresIn()));
                    Configuration.tokenExpiry = cal.getTime();
                    SMSReceiver.this.sendSMS(context,SMSReceiver.this.getMobileNumber(context),body);
                }

                public void onFailure(HttpContext httpcontext, Throwable error) {
                    Configuration.oAuthAccessToken = "";
                    Configuration.tokenExpiry = new Date(0);
                    Log.i("getAccessToken", "Get token failed, reset Configuration.oAuthAccessToken");
                }
            });
        }
    }

    private void sendSMS(Context context,String to, String body){
        APIController controller = new APIController();
        SendMessageRequest messageRequest = new SendMessageRequest();
        messageRequest.setTo(to);
        messageRequest.setBody(body);

        try {
            controller.createSMSMessageAsync(messageRequest,
                    new APICallBack<SendMessageResponse>() {
                        public void onSuccess(HttpContext context, SendMessageResponse response) {
                            Log.i("sendSMS", response.getMessageId());
                        }

                        public void onFailure(HttpContext context, Throwable error) {
                            Log.i("sendSMS", "SMSReceiver.sendSMS.onFailue");
                        }
                    });
        } catch (JsonProcessingException error) {
            Toast.makeText(context,
                    "sendSMS JsonProcessingException = " + error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Toast.makeText(context,
                "SMS forwarded\nToken Expiry = " + dateFormat.format(Configuration.tokenExpiry),
                Toast.LENGTH_LONG).show();
    }


    private void sendSelfSMS(Context context, String body){
        Configuration.initialize(context);
        APIController controller = new APIController();
        String token = controller.getAccessTokenSync();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        token += "\n" + dateFormat.format(Configuration.tokenExpiry);
        Log.i("onReceive", "token = " + token);
        Log.i("onReceive", "token expiry = " + dateFormat.format(Configuration.tokenExpiry));

        try {
            controller.sendSelfSMSAsync(body,
                    new APICallBack<SendMessageResponse>() {
                        public void onSuccess(HttpContext context, SendMessageResponse response) {
                            Log.i("sendSelfSMS", response.getMessageId());
                        }

                        public void onFailure(HttpContext context, Throwable error) {
                            Log.i("sendSelfSMS", "12312saaasdfasfsd");
                        }
                    });
        } catch (JsonProcessingException error) {
            Toast.makeText(context,
                    "sendSelfSMS JsonProcessingException = " + error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        Toast.makeText(context,
                "SMS forwarded\nToken = " + token,
                Toast.LENGTH_LONG).show();
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