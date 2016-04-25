package com.hdu.team.hiwanan.util;

/**
 * Created by JerryYin on 11/4/15.
 */
public class HiConstants {

    /**
     * sharedPrf数据库名称
     * 全局数据库，只有这一个sp数据库
     */
    public final static String HI_PREFERENCE_NAME = "hi_pref";

    /* 测试地址*/
    public final static String TEST_URL = "http://192.168.2.2000/Wanan/UpVoice";


    /**
     * Hi语音性别键值对
     * 存储到sprf数据库的key_value
     */
    public final static String KEY_SEX_DIF = "sex_diff_only";
    public final static String KEY_SEX_SAME = "sex_same_only";
    public final static String KEY_SEX_ALL = "sex_all";


    /**
     * 锁屏，解锁服务 参数
     */
    public final static String LOCK_SCREEN = "0X11";
    public final static String UN_LOCK_SCREEN = "0X12";


    /**
     * 闹钟时差（分 秒）
     */
    public static final String DIF_HOURS = "dif_hours";
    public static final String DIF_MINUTES = "dif_minutes";;
}
