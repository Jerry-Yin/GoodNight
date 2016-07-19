package com.hdu.team.hiwanan.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.manager.SystemBarTintManager;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by JerryYin on 11/3/15.
 * 本应用的基类Activity
 */
public abstract class HiActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "HiActivity";

//    public static Toast mToast;

    public AlertDialog mAlertDialog;
    public AlertDialog.Builder mBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 设置竖屏
         */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /**
         * 如果当前sdk版本 >＝ 19, 就采用自定制状态栏
         * 初此处修改之外，还需要
         * 1.在所有需要此状态栏的Activity的对应界面布局中 添加属性标签：android:fitsSystemWindows="true"
         * 2.在res/values-v19/styles.xml 文件复制低版本的所有属性
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            /** 自定制的系统状态栏颜色*/
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        }


        initBmobSdk();
        initDialog();
    }

    private void initDialog(){
        if (mAlertDialog == null){
            mAlertDialog = new AlertDialog.Builder(this).create();
        }
        if (mBuilder == null){
            mBuilder = new AlertDialog.Builder(this);
        }
    }

    private void initBmobSdk() {
        //第一：默认初始化
        Bmob.initialize(this, HiConfig.APPLICATION_ID);

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
//        BmobConfig config =new BmobConfig.Builder(this)
//        //设置appkey
//        .setApplicationId(HiConfig.APPLICATION_ID)
//        //请求超时时间（单位为秒）：默认15s
//        .setConnectTimeout(30)
//        //文件分片上传时每片的大小（单位字节），默认512*1024
//        .setUploadBlockSize(1024*1024)
//        //文件的过期时间(单位为秒)：默认1800s
//        .setFileExpiration(2500)
//        .build();
//        Bmob.initialize(config);

    }




//    public static void HiStartActivity(Activity a, Class toActivity){
//        Intent intent = new Intent(a, toActivity);
//        startActivity(intent);
//    }

//    public static showToast(String msg){
//        HiLog.d("mToast="+mToast);
//        if (mToast != null){
//            mToast.cancel();
//        }
//        mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
//        mToast.show();
//    }



}
