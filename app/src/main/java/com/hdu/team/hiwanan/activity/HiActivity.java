package com.hdu.team.hiwanan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.hdu.team.hiwanan.util.HiLog;

/**
 * Created by JerryYin on 11/3/15.
 * 本应用的基类Activity
 */
public abstract class HiActivity extends Activity {

    public final String TAG = "HiActivity";

//    public static Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


//    public static showToast(String msg){
//        HiLog.d("mToast="+mToast);
//        if (mToast != null){
//            mToast.cancel();
//        }
//        mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
//        mToast.show();
//    }

}
