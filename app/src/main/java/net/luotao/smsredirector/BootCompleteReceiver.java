package net.luotao.smsredirector;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import net.luotao.smsredirector.SettingsActivity;

public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String BOOT_COMPLETED_ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BootCompleteReceiver", "BootCompleteReceiver onReceive() was called");
        if (BOOT_COMPLETED_ACTION.equals(intent.getAction())) {
            Intent mServiceIntent = new Intent();
//            mServiceIntent.setAction("com.android.myalarm.SetAlarmsService");
//            ComponentName service = context.startService(mServiceIntent);

            Intent i = new Intent(context, SettingsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

            Toast.makeText(context,
                    "BootCompleteReceiver",
                    Toast.LENGTH_LONG).show();

//            Intent intent2 = new Intent(context, SMSReceiver.class);
//            context.startService(intent2);
        }
    }
}

