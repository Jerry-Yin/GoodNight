package com.hdu.team.hiwanan.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hdu.team.hiwanan.service.HiRingtoneService;
import com.hdu.team.hiwanan.service.HiRingtoneService2;
import com.hdu.team.hiwanan.util.HiLog;

/**
 * Created by KaikaiFu on 2017/3/12.
 */

public class HiAlarmClockReceiver extends BroadcastReceiver {

    private static final String TAG = "HiAlarmClockReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        HiLog.d(TAG, "onReceiver..");

        boolean switchState = false;
        String category = null;
        Integer alarmId = 0;

        if (intent.hasExtra("switch")) {
            switchState = intent.getExtras().getBoolean("switch");
        }
        // switch state from clock frame
        if (intent.hasExtra("category")) {
             category = intent.getExtras().getString("category");
        }
        if (intent.hasExtra("id")){
            alarmId = intent.getExtras().getInt("id");
        }

        HiLog.d(TAG, "onReceive: " + category);
        HiLog.d(TAG, "switch state in receiver: " + switchState);
        HiLog.d(TAG, "alarm id : "+ alarmId);
        Intent serviceIntent = new Intent(context, HiRingtoneService2.class);
        serviceIntent.putExtra("switch", switchState);
        serviceIntent.putExtra("category", category);
        serviceIntent.putExtra("id", alarmId);
        context.startService(serviceIntent);

    }
}
