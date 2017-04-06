package com.hdu.team.hiwanan.constant;

import android.os.Environment;

/**
 * Created by JerryYin on 7/13/16.
 */
public class HiConfig {


    /**
     *  app数据存储路径
     */
    public static final String APP_DIR = Environment.getExternalStorageDirectory() + "/HiWanan";
    public static final String APP_TEMP = APP_DIR + "/temp";
    public static final String APP_IMAGE = APP_DIR + "/image";
    public static final String APP_VOICE_DIR = Environment.getExternalStorageDirectory() + "/hiwanan_voice_audios";   //录音文件

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
     * 闹钟时差（分 秒）
     */
    public static final String DIF_HOURS = "dif_hours";
    public static final String DIF_MINUTES = "dif_minutes";


    //TODO:kaikai added constants

    /**
     * switch of each alarm clock status key name
     */

    public static final String SWITCH_STATUS = "status";
    public static final String STATUS_NUM = "status_num";

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
    public static final String KEY_ICON = "icon";

    /**
     * 默认用户头像URL
     */
    public static final String USER_ICON_M_S = "http://bmob-cdn-4793.b0.upaiyun.com/2016/07/14/938fe7af40e67ab680670a9a4881a4e1.png";
    public static final String USER_ICON_M_W = "http://bmob-cdn-4793.b0.upaiyun.com/2016/07/14/6f9e6d0b405d3e79809f04e32ec772e5.png";
    public static final String USER_ICON_W_S = "http://bmob-cdn-4793.b0.upaiyun.com/2016/07/14/6a0d846a400aec6b804c0de660c02953.png";
    public static final String USER_ICON_W_W = "http://bmob-cdn-4793.b0.upaiyun.com/2016/07/14/39be4f5d4043b1c880095d179b4eaf5b.png";



    /**
     * 锁屏，解锁服务 参数
     */
    public final static String LOCK_SCREEN = "0X11";
    public final static String UN_LOCK_SCREEN = "0X12";


    /**
     * 聚合数据提供
     * 日历数据请求接口
     * 1。 万年历请求api
     * 请求示例：http://japi.juhe.cn/calendar/day?date=2015-1-1&key=您申请的appKey
     * 注意 * date=yyyy-m-d   (m d  不能前面加0， 个位数救赎个位数)
     */
    public static final String APP_KEY_CALENDAR = "ced957858d6d3f18b18c071b837a3966";
    public static final String URL_CALENDAR = "http://v.juhe.cn/calendar/day";
    String test = "http://v.juhe.cn/calendar/day?date=2017-3-10&key=ced957858d6d3f18b18c071b837a3966";

    /**
     * @author Kaikai
     * 闹钟设置相关
     * 确定闹钟修改还是新建行为
     */

    public static final String REQUEST_TYPE = "TYPE";
    public static final int CREATE_REQUEST =  0x31;
    public static final int MODIFY_REQUEST = 0x32;

    /**
     * @author Kaikai
     * 判断是否第一次使用本软件
     */
    public static final String FIRST_LOAD = "FIRST_LOAD";


    /**
     * 系统点亮和熄灭屏幕的广播
     * 其中锁屏广播action我们自定义，因为高版本不允许直接发送系统这条锁屏的广播
     */
    public static final String ACTION_SCREEN_ON = "android.intent.action.SCREEN_ON";
    public static final String ACTION_SCREEN_OFF = "android.intent.action.SCREEN_OFF_hi";
}
