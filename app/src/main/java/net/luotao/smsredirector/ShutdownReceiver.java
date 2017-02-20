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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.telstra.api.Configuration;
import com.telstra.api.controllers.APIController;
import com.telstra.api.http.client.APICallBack;
import com.telstra.api.http.client.HttpContext;
import com.telstra.api.models.SendMessageRequest;
import com.telstra.api.models.SendMessageResponse;


public class ShutdownReceiver extends BroadcastReceiver {
    private static final String MOBILE_NUMBER_KEY = "receive_number_text";

    private String getMobileNumber(Context context) {
        String mobileNumber = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(MOBILE_NUMBER_KEY, "");
        return mobileNumber;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        APIController controller = new APIController();
        SendMessageRequest messageRequest = new SendMessageRequest();
        messageRequest.setTo(Configuration.selfMobileNumber);
        messageRequest.setBody(android.os.Build.MODEL + " Power Off");

        try {
            controller.createSMSMessageAsync(messageRequest,
                    new APICallBack<SendMessageResponse>() {
                        public void onSuccess(HttpContext context, SendMessageResponse response) {
                        }

                        public void onFailure(HttpContext context, Throwable error) {
                        }
                    });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
