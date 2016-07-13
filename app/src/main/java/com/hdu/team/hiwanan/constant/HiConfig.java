package com.hdu.team.hiwanan.constant;

/**
 * Created by JerryYin on 7/13/16.
 */
public class HiConfig {
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


    /**
     * 应用－Bmob相关密钥信息
     */
    public static final String APPLICATION_ID = "cdb9029bf4dee23edca7401bcd724bd6";
    public static final String RESET_API_KEY = "0016b75f208958585836c79d960c8428" ;
    public static final String SECRET_KEY = "5eff29f8b71e47d5";
    public static final String MASTER_KEY = "e000e1833aa18ae131409b49aa972b7c";


    /**
     * 注册相关
     */
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_PASSWORD = "password";


}
