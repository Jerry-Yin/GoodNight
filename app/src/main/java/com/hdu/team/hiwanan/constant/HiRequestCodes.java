package com.hdu.team.hiwanan.constant;

/**
 * Created by JerryYin on 7/13/16.
 * 请求返回参数对应标志
 */
public class HiRequestCodes {

    /**
     * 注册相关
     */
    public static final int REGIST_SUCCESS = 0X01;
    public static final int REGIST_FAIL = 0X02;

    /**
     * 登录相关
     */
    public static final int LOGIN_SUCCESS = 0X04;
    public static final int LOGIN_FAIL = 0X05;

    /**
     * 下载
     */
    public static final int DOWNLOAD_SUCCESS = 0X08;
    public static final int DOWNLOAD_FAIL = 0X09;
    public static final int DOWNLOAD_PROGRESS = 0X10;


    /**
     * 拍照&图片选择
     */
    public static final int IMAGE_OPEN = 0X14;
    public static final int TAKE_PHOTO = 0X15;

    /**
     * 上传文件
     */
    public static final int UPLOAD_ICON_SUCCESS = 0X18;
    public static final int UPLOAD_PROGRESS = 0X19;
    public static final int UPLOAD_FAIL = 0X20;

    /***
     * 更新用户信息
     */
    public static final int UPDATE_ICON_SUCCESS = 0X21;
    public static final int UPDATE_NAME_SUCCESS = 0X22;
    public static final int UPDATE_LEVEL_SUCCESS = 0X23;
    public static final int UPDATE_GROUP_SUCCESS = 0X24;
    public static final int UPDATE_FAIL = 0X25;



    /**
     * 倒计时
     */
    public static final int COUNT_DOWN = 0X30;

}
