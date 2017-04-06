package com.hdu.team.hiwanan.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PowerManager;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.activity.HiMainActivity;
import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.util.HiLog;

import java.security.PublicKey;

/**
 * Created by JerryYin on 3/29/17.
 * 自定义的后台闹钟任务线程
 */

public class HiAlarmTask implements Runnable {

    private static final String TAG = "HiAlarmTask";

    private Context context;
    private int taskId;         //任务id = 闹钟id = position
    private MediaPlayer player; //媒体播放器
    private int musicId;        //musicId



    public HiAlarmTask(Context c, int taskId, int musicId) {
        this.context = c;
        this.taskId = taskId;
        if (player == null){
            player = MediaPlayer.create(c, musicId);
        }
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int id) {
        this.taskId = id;
    }

    public MediaPlayer getPlayer() {
        return player;
    }


    /**
     * 1. wake up device
     * 2. play music
     * 3. show the screen shot page
     */
    @Override
    public void run() {
        HiLog.d(TAG, "alarmTask"+taskId +" is start...");
        player.start();
        sendScreenLockBroadcast();
    }

    /**
     * 闹钟响起的时候发送锁屏广播，启动锁屏
     */
    private void sendScreenLockBroadcast() {
        Intent i2 = new Intent(HiConfig.ACTION_SCREEN_OFF);
        context.sendBroadcast(i2);
    }


    /**
     * 1. stop music
     * 2. destroy object
     *
     */
    public void shutdown(){
        HiLog.d(TAG, "alarmTask"+taskId +" is shutdown...");
        if (player != null && player.isPlaying()){
            player.stop();
            player.reset();
        }

    }



}
