package com.hdu.team.hiwanan.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hdu.team.hiwanan.service.HiRingtoneService;

/**
 * Created by KaikaiFu on 2017/3/12.
 */

public class HiAlarmClockReceiver extends BroadcastReceiver{
    private static final String TAG = "HiAlarmClockReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "in the receiver");
        // switch state from clock frame
        boolean switchState = intent.getExtras().getBoolean("switch");

        String category = intent.getExtras().getString("category");

        Log.d(TAG, "onReceive: " + category);
        Log.d(TAG, "switch state in receiver" + switchState);
        Intent serviceIntent = new Intent(context, HiRingtoneService.class);
        serviceIntent.putExtra("switch", switchState);
        serviceIntent.putExtra("category", category);
        context.startService(serviceIntent);

    }
}
