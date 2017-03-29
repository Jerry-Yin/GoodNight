package com.hdu.team.hiwanan.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.activity.HiMainActivity;
import com.hdu.team.hiwanan.manager.HiAlarmTaskPoolManager;
import com.hdu.team.hiwanan.model.HiAlarmTask;
import com.hdu.team.hiwanan.util.HiLog;

/**
 * Created by KaikaiFu on 2017/3/12.
 */

public class HiRingtoneService2 extends Service {

    private static final String TAG = "HiRingtoneService2";


//    private List<HiAlarmThread> mHiAlarmThreads = new ArrayList<>();
//    private HiAlarmThreadManager mHiAlarmThreadManager = HiAlarmThreadManager.getInstance();

    private HiAlarmTaskPoolManager mTaskPoolManager = HiAlarmTaskPoolManager.getInstance();

//    private MediaPlayer mediaPlayer;
//    int startId;
//    boolean isPlaying;
    //String currentCategory;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        HiLog.i(TAG, "onStartCommand() start id" + startId + ":" + intent);
        String category = intent.getStringExtra("category");
        boolean state = intent.getExtras().getBoolean("switch");
        int id = intent.getExtras().getInt("id");

        // create alarmtask if is not null

        HiAlarmTask alarmTask = mTaskPoolManager.getTaskById(id);
        if (alarmTask == null) {
            alarmTask = new HiAlarmTask(getApplicationContext(), id, R.raw.voice);
        }
//        if (intent.hasExtra("category")) {
//            category = intent.getStringExtra("category");
//        }
//        if (intent.hasExtra("switch")) {
//            state = intent.getExtras().getBoolean("switch");
//        }
//        if (intent.hasExtra("id")) {
//            id = intent.getExtras().getInt("id");
//        }
        if (state) {
            mTaskPoolManager.executeTask(alarmTask);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent intentMain = new Intent(this.getApplicationContext(), HiMainActivity.class);
            PendingIntent pendingMainIntent = PendingIntent.getActivity(this, 0, intentMain, 0);


            PowerManager pm = (PowerManager) this.getApplicationContext().getSystemService(Context.POWER_SERVICE);
            //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
            //点亮屏幕
            wl.acquire();
            //释放
            wl.release();

            Notification notificationPopup = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("An alarm is going off")
                    .setContentText("click me!")
                    .setContentIntent(pendingMainIntent)
                    .setAutoCancel(true)
                    .build();

            notificationManager.notify(0, notificationPopup);


        } else if (!state) {
            mTaskPoolManager.removeTask(alarmTask);
        }


        return START_NOT_STICKY;

    }


    public void onDestroy() {
        super.onDestroy();
    }
}
