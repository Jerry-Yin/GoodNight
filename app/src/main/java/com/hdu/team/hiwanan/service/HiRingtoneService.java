package com.hdu.team.hiwanan.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.activity.HiMainActivity;

/**
 * Created by KaikaiFu on 2017/3/12.
 */

public class HiRingtoneService extends Service {
    private MediaPlayer mediaPlayer;
    int startId;
    boolean isPlaying;
    //String currentCategory;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("local service", "received start id" + startId + ":" + intent);
        boolean state = intent.getExtras().getBoolean("switch");
        //String category = intent.getExtras().getString("category");
          if (state == true) {
              startId = 1;
          } else {
              startId=0;
          }



        if (!this.isPlaying && startId == 1 ){
            Log.e("this is no music", "and you want on");
            mediaPlayer = MediaPlayer.create(this, R.raw.voice);
            mediaPlayer.start();

            this.isPlaying = true;
            this.startId = 0;

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent intentMain = new Intent(this.getApplicationContext(), HiMainActivity.class);
            PendingIntent pendingMainIntent = PendingIntent.getActivity(this,0,intentMain,0);


            PowerManager pm=(PowerManager) this.getApplicationContext().getSystemService(Context.POWER_SERVICE);
            //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
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

            notificationManager.notify(0,notificationPopup);


        }
        else if(this.isPlaying && startId == 0){
            Log.e("this is music", "and you want off");
            mediaPlayer.stop();
            mediaPlayer.reset();
            this.isPlaying = false;
            this.startId=0;

        }
        else if(!this.isPlaying && startId == 0) {
            Log.e("this is no music", "and you want off");
            this.isPlaying = false;
            this.startId = 0;
        }
        else if(this.isPlaying && startId == 1) {
            Log.e("this is music", "and you want on");
            this.isPlaying = true;
            this.startId = 0;

        }
        else {
            Log.e(" otherwise", "you reached this");
        }


        return START_NOT_STICKY;

    }


    public void onDestroy(){

        super.onDestroy();
        this.isPlaying = false;
    }
}
