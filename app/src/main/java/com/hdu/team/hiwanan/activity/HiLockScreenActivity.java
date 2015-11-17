package com.hdu.team.hiwanan.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hdu.team.hiwanan.R;

/**
 * Created by JerryYin on 11/16/15.
 */
public class HiLockScreenActivity extends HiActivity {

    private ImageView mbtnUnLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        openSystemScreenLock();

        setContentView(R.layout.layout_lock_screen);

        mbtnUnLock = (ImageView) findViewById(R.id.btn_unlock);
        mbtnUnLock.setOnClickListener(this);
    }

    /**
     * 启动自己的锁屏之前，先打开系统的锁屏
     * 要在setContentView()前面；
     */
//    private void openSystemScreenLock() {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_unlock){

        }
    }

    /**
     * 屏蔽返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }
}
