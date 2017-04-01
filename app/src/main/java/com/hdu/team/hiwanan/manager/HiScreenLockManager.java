package com.hdu.team.hiwanan.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by JerryYin on 3/30/17.
 */

public class HiScreenLockManager {

    private Context mContext;
    private ScreenLockListener mScreenLockListener;
    private HiScreenLockBdReceiver mScreenLockBdReceiver;

    private static HiScreenLockManager mInstance;

    private HiScreenLockManager(Context c){
        this.mContext = c;
        mScreenLockBdReceiver = new HiScreenLockBdReceiver();
    }


    public synchronized  static HiScreenLockManager getInstance(Context c){
        if(mInstance == null){
            mInstance = new HiScreenLockManager(c);
        }
        return mInstance;
    }

    public void register(ScreenLockListener listener){
        if (listener != null)
            mScreenLockListener = listener;
        if (mScreenLockBdReceiver != null){
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
//            filter.addAction(Intent.ACTION_USER_PRESENT);
            mContext.registerReceiver(mScreenLockBdReceiver, filter);
        }
    }


    public void unRegister(){
        if (mScreenLockBdReceiver != null)
            mContext.unregisterReceiver(mScreenLockBdReceiver);
    }


    public interface ScreenLockListener{
        void onScreenON();
        void onScreenOFF();
    }

    /**
     * 接收系统锁屏解锁的广播
     */
    public class HiScreenLockBdReceiver extends BroadcastReceiver {


        private static final String TAG = "HiScreenLockBdReceiver";


        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                switch (action) {
                    case Intent.ACTION_SCREEN_OFF:
                        if (mScreenLockListener != null){
                            mScreenLockListener.onScreenOFF();
                        }
                        break;

                    case Intent.ACTION_SCREEN_ON:
                        if (mScreenLockListener != null){
                            mScreenLockListener.onScreenON();
                        }
                        break;
//                    case Intent.ACTION_USER_PRESENT:
//                        if (mScreenLockListener != null){
//                            mScreenLockListener.onScreenOFF();
//                        }
//                        break;

                    default:
                        break;
                }
            }
        }
    }

}
