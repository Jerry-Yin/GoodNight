package com.hdu.team.hiwanan.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by JerryYin on 5/26/16.
 */
public class ToastUtils {


    public static void showToast(Context c, String content, int time){
        Toast.makeText(c, content, time).show();
    }


}
