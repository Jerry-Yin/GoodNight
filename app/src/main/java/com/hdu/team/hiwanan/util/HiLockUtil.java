package com.hdu.team.hiwanan.util;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by JerryYin on 4/6/17.
 */

public class HiLockUtil {

    private Context mContext;
    private WindowManager mWindowManager;
    private View mLockView;
    private WindowManager.LayoutParams mLayoutParams;
    private boolean isLocked;
    private static HiLockUtil mInstance;


    private HiLockUtil(Context c){
        this.mContext = c;
        init();
    }

    public synchronized static HiLockUtil getInstance(Context c){
        if (mInstance == null)
            mInstance = new HiLockUtil(c);
        return mInstance;
    }

    private void init() {
        isLocked = false;
        mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        mLayoutParams.format = PixelFormat.RGBA_8888;   //图片格式，效果为 背景透明
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.flags = 1280;
    }

    public synchronized void setLockView(View view){
        this.mLockView = view;
    }

    /**
     * 锁屏
     */
    public synchronized void lockScreen(){
        if (mLockView != null && !isLocked){
            mWindowManager.addView(mLockView, mLayoutParams);
        }
        isLocked = true;
    }

    /**
     * 解锁
     */
    public synchronized void unLockScreen(){
        if (mWindowManager != null && isLocked){
            mWindowManager.removeView(mLockView);
        }
        isLocked = false;
    }

    public synchronized void updateLockView(){
        if (mLockView != null && !isLocked){
            mWindowManager.updateViewLayout(mLockView, mLayoutParams);
        }
    }


    /**
     * 在Activity中调用示例
     * 自定义View可以传一个Handler参数，方便从自定义View中传递数据到Activity中
     *
     * lockView = View.inflate(this, R.layout.main, null);
     * lockLayer = new LockUtil(this);
     * lockLayer.setLockView(lockView);// 设置要展示的页面
     * lockLayer.lock();// 开启锁屏
     * CustomView.setHandler(mHandler);
     */
}
