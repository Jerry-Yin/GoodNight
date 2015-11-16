package com.hdu.team.hiwanan.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.hdu.team.hiwanan.broadcast.HiBroadcastReceiver;
import com.hdu.team.hiwanan.util.HiConstants;


/**
 * Created by JerryYin on 11/16/15.
 * 通过此服务定时发送广播，启动锁频
 */
public class HiLockScreenService extends Service {


    private Intent mStartIntent;
    private IntentFilter mIntentFilter;
    private HiBroadcastReceiver mReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    /**
     * 启动服务后，注册广播接收器
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mStartIntent = intent;
        mIntentFilter = new IntentFilter();

//        switch (intent.getAction()) {
//            case HiConstants.LOCK_SCREEN:
//                //锁屏
//                mIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
//                break;
//
//            case HiConstants.UN_LOCK_SCREEN:
//                //解锁
//                mIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
//                break;
//
//            default:
//                break;
//        }

        mIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        mIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);

        mReceiver = new HiBroadcastReceiver();
        registerReceiver(mReceiver, mIntentFilter);

        System.out.println("service onStart and action is " + intent.getAction());
        System.out.println("service onStart and startId is " + startId);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 保证服务长期运行
     */
    @Override
    public void onDestroy() {
        System.out.println("service onDestroy");
        unregisterReceiver(mReceiver);

        //保留了开启service的intent，在这里再启动一次自己，以达到长期运行的服务，不被系统杀死
        if (mStartIntent != null) {
            System.out.println("serviceIntent not null");
//            startService(mStartIntent);
        }
    }
}
