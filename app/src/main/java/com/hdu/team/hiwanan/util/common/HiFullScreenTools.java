package com.hdu.team.hiwanan.util.common;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by JerryYin on 11/16/15.
 */
public class HiFullScreenTools {


    public static void updateFullscreenStatus(Activity activity, Boolean fullscreen){
        if (fullscreen){
            //全屏显示
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }else {
            //已经进入主界面，需要取消全屏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

    }

}
