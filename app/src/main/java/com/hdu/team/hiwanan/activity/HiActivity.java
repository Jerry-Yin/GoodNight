package com.hdu.team.hiwanan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.manager.SystemBarTintManager;
import com.hdu.team.hiwanan.util.HiLog;

/**
 * Created by JerryYin on 11/3/15.
 * 本应用的基类Activity
 */
public abstract class HiActivity extends Activity implements View.OnClickListener{

    public final String TAG = "HiActivity";

//    public static Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
