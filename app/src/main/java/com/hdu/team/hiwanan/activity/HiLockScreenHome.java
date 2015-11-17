package com.hdu.team.hiwanan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.ListAdapter;

import com.hdu.team.hiwanan.util.HiConstants;
import com.hdu.team.hiwanan.util.HiLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryYin on 11/17/15.
 * 锁屏状态下 按home键，跳转到此界面，但表现为没反应， 达到屏蔽的效果；
 * 非锁屏状态下按home键，仍然跳抓到此界面，但只需要在onCreate()方法设置即可：
 */
public class HiLockScreenHome extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //跳转到系统主屏幕;
//        startSystemHome();

        HiLog.e("", "home is called");
        finish();   // 调用finish以防止程序卡在一个不展示的activity

    }

    /**
     * 首先，获取一个列表：列出所以可以作为主屏幕的程序;
     * 然后，可以用alertDialog的方式让用户选择要跳到哪个主屏幕，并用sharedPreferences记录用户的选择。
     */
    private void startSystemHome() {
        final List<String> pkgNamesT = new ArrayList<String>();
        final List<String> actNamesT = new ArrayList<String>();
        List<ResolveInfo> resolveInfos = this.getPackageManager().queryIntentActivities(new Intent(), PackageManager.MATCH_DEFAULT_ONLY);
        for (int i = 0; i < resolveInfos.size(); i++) {
            String string = resolveInfos.get(i).activityInfo.packageName;
            if (!string.equals(this.getPackageName())) {//排除自己的包名
                pkgNamesT.add(string);
                string = resolveInfos.get(i).activityInfo.name;
                actNamesT.add(string);
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences(HiConstants.HI_PREFERENCE_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        new AlertDialog.Builder(this).setTitle("请选择解锁后的屏幕").setCancelable(false).setSingleChoiceItems((ListAdapter) actNamesT, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                editor.putString("packageName", pkgNamesT.get(which));
                editor.putString("activityName", actNamesT.get(which));
                editor.commit();
//                originalHome();
                dialog.dismiss();
            }
        }).show();


        /**
         * 这样，下次的时候就可以根据包名和类名，直接跳转到用户所设置的主屏幕了：
         */
        String pkgName = sharedPreferences.getString("packageName", null);
        String actName = sharedPreferences.getString("activityName", null);
        ComponentName componentName = new ComponentName(pkgName, actName);
        Intent intent = new Intent();
        intent.setComponent(componentName);
        this.startActivity(intent);
        ((Activity) this).finish();
    }
}
