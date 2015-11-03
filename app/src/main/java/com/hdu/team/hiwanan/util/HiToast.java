package com.hdu.team.hiwanan.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiToast {

    public static Toast mToast;


    public static void showToast(Context minstance, String msg){
        HiLog.d("mToast="+mToast);
        if (mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(minstance, msg, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
