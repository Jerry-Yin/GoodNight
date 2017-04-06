package com.hdu.team.hiwanan.util;

import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * Created by JerryYin on 4/5/17.
 */

public class HiNotificationUtil {



    /**
     * 从Android 4.3（api 18）开始，Google给我们提供了一个NotificationListenerService类，
     * 第三方应用可以更方便的获得通知栏使用权（Notification Access），当然，这么敏感的权限得要应用自己声明，同时还要引导用户手动授权
     *
     * 检查通知栏使用权是否已经拿到
     * @param context
     * @return
     */
    public static boolean isNotificationListenEnabled(Context context) {
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(),
                "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
