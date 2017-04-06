package com.hdu.team.hiwanan.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.hdu.team.hiwanan.activity.HiScreenLockActivity;
import com.hdu.team.hiwanan.broadcast.HiBroadcastReceiver;
import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.util.HiLog;


/**
 * Created by JerryYin on 11/16/15.
 * 通过此服务定时发送广播，启动锁频
 */
public class HiScreenLockService extends Service {


    private static final String TAG = "HiScreenLockService";

    private ScreenOnReceiver mOnReceiver;
    private ScreenOffReceiver mOffReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mOnReceiver = new ScreenOnReceiver();
        IntentFilter filterOn = new IntentFilter(HiConfig.ACTION_SCREEN_ON);
        registerReceiver(mOffReceiver, filterOn);

        mOffReceiver = new ScreenOffReceiver();
        IntentFilter filterOff = new IntentFilter(HiConfig.ACTION_SCREEN_OFF);
        registerReceiver(mOffReceiver, filterOff);
    }

    /**
     * 启动服务后，注册广播接收器
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    /**
     * 保证服务长期运行
     */
    @Override
    public void onDestroy() {
        System.out.println("service onDestroy");
        unregisterReceiver(mOffReceiver);
        unregisterReceiver(mOnReceiver);
    }


    /**
     * 监听屏幕变亮的广播接收器，变亮就屏蔽系统锁屏
     * @author tongleer.com
     *
     */
    private class ScreenOnReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(HiConfig.ACTION_SCREEN_ON)){
                /*
                 * 此方式已经过时，在activtiy中编写
                 * getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                 * getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                 * 两句可以代替此方式
                 */
                KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock("hi_keyguard");
                lock.disableKeyguard();
            }
        }
    }

    /**
     * 监听屏幕变暗的广播接收器，变暗就启动应用锁屏界面activity
     * @author tongleer.com
     *
     */
    private class ScreenOffReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(HiConfig.ACTION_SCREEN_OFF)){
                Intent i1=new Intent(context,HiScreenLockActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i1);
            }
        }
    }
}
