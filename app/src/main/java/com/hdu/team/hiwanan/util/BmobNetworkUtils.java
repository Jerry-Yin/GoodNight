package com.hdu.team.hiwanan.util;

import android.content.Context;

import com.hdu.team.hiwanan.listener.OnResponseListener;
import com.hdu.team.hiwanan.model.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by JerryYin on 7/13/16.
 * 采用 Bmob 方式的网络请求工具类
 */
public class BmobNetworkUtils {

    /**
     * 用户注册
     * @param user
     * @param listener
     */
    public static void signUp(User user, final OnResponseListener listener){
        if (user != null){
            user.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null){
                        if (listener != null){
                            listener.onSuccess(s);
                        }
                    }else {
                        if (listener != null){
                            listener.onFailure(e.getErrorCode(), e.getMessage());
                        }
                    }
                }
            });
        }
    }





}
