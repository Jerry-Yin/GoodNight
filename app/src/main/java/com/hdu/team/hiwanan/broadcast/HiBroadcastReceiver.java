package com.hdu.team.hiwanan.broadcast;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hdu.team.hiwanan.activity.HiLockScreenActivity;
import com.hdu.team.hiwanan.util.HiToast;

/**
 * Created by JerryYin on 11/16/15.
 */
public class HiBroadcastReceiver extends BroadcastReceiver {

    private Intent mLockIntent;


    /**
     * 接受到广播，启动锁屏
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        System.out.println("action is "+action);
        HiToast.showToast(context, "服务启动，接受到广播，跳转至锁屏"+ "action = "+action);

        /**
         * 替换掉（解锁）系统的锁屏
         * 需要添加对应权限
         */
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock  = keyguardManager.newKeyguardLock("my_lockscreen");
        keyguardLock.disableKeyguard();

        /**
         * 跳转到自己的锁屏界面
         * 为了防止主界面被重复调用，我们在设置intent时还要加上一些filter：
         */
        mLockIntent = new Intent(context, HiLockScreenActivity.class);
        // 没有加的时候系统会报错，但是加上以后也有问题，这会导致多次退出才能退出自定义的锁屏界面。
        // 其实可以自定义一个stack来管理这些activity，
        mLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mLockIntent);
    }

}
