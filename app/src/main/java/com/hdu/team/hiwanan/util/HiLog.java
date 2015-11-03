package com.hdu.team.hiwanan.util;

import android.util.Log;

/**
 * Created by JerryYin on 11/3/15.
 * 自己的Log工具类
 */
public class HiLog {

//    public static boolean D = false;
    public static boolean D = true;
    private static final String DTAG = "hiwanan_fastlog";



    public static void v(String tag, String msg){
        if (D){
            Log.v(tag, msg);
        }
    }

    public static void v(String msg){
        if (D){
            Log.v(DTAG, msg);
        }
    }

    public static void d(String tag,String msg){
        if(D){
            Log.d(tag, msg);
        }
    }
    public static void d(String msg){
        if(D){
            Log.d(DTAG, msg);
        }
    }

    public static void i(String tag,String msg){
        if(D){
            Log.i(tag, msg);
        }
    }
    public static void i(String msg){
        if(D){
            Log.i(DTAG, msg);
        }
    }

    public static void w(String tag,String msg){
        if(D){
            Log.w(tag, msg);
        }
    }
    public static void w(String msg){
        if(D){
            Log.w(DTAG, msg);
        }
    }

    public static void e(String tag,String msg){
        if(D){
            Log.e(tag, msg);
        }
    }
    public static void e(String msg){
        if(D){
            Log.e(DTAG, msg);
        }
    }



}
