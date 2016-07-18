package com.hdu.team.hiwanan.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.hdu.team.hiwanan.listener.OnProgressListener;
import com.hdu.team.hiwanan.listener.OnResponseListener;
import com.hdu.team.hiwanan.model.User;
import com.hdu.team.hiwanan.model.UserBmob;
import com.hdu.team.hiwanan.network.OnDownloadListener;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by JerryYin on 7/13/16.
 * 采用 Bmob 方式的网络请求工具类
 */
public class BmobNetworkUtils {

    /**
     * 用户注册
     *
     * @param user
     * @param listener
     */
    public static void signUp(User user, final OnResponseListener listener) {
        if (user != null) {
            user.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        if (listener != null) {
                            listener.onSuccess(s);
                        }
                    } else {
                        if (listener != null) {
                            listener.onFailure(e.getErrorCode(), e.getMessage());
                        }
                    }
                }
            });
        }
    }

    /**
     * 用户注册方式2
     * 采用 BmobUser 对象的方法
     *
     * @param user
     * @param listener
     */
    public static void signUp(final UserBmob user, final OnResponseListener listener) {
        user.signUp(new SaveListener<UserBmob>() {
            @Override
            public void done(UserBmob userBmob, BmobException e) {
                if (e == null) {
                    if (listener != null) {
                        listener.onSuccess(userBmob);
                    }
                } else {
                    //特殊错误：gson 转换错误，估计是bmob后台写错了，但是注册是成功的
                    // code = 9015
                    // error =com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected a string but was BEGIN_OBJECT at line 1 column 2 path $
                    if (listener != null) {
                        if (e.getErrorCode() == 9015 && e.getMessage().equals("com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected a string but was BEGIN_OBJECT at line 1 column 2 path $")) {
                            listener.onSuccess("success");
                        } else {
                            listener.onFailure(e.getErrorCode(), e.getMessage());
                        }
                    }
                }
            }
        });
    }


    public static void signIn() {

    }

    /**
     * 用户登录方式2
     *
     * @param user
     * @param listener
     */
    public static void signIn(UserBmob user, final OnResponseListener listener) {
        user.login(new SaveListener<UserBmob>() {
            @Override
            public void done(UserBmob user, BmobException e) {
                if (e == null) {
                    if (listener != null) {
                        //登录成功，返回当前登录用户
                        listener.onSuccess(BmobUser.getCurrentUser(UserBmob.class));
                    }
                } else {
                    if (listener != null) {
                        listener.onFailure(e.getErrorCode(), e.getMessage());
                    }
                }
            }
        });


    }


    /**
     * 下载文件存储到本地的方法
     *
     * @param context
     * @param bmobFile
     * @param listener
     */
    public static void downloadIcon(Context context, BmobFile bmobFile, final OnDownloadListener listener) {
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File savePath = new File(context.getApplicationContext().getCacheDir() + "/icon/", bmobFile.getFilename());
        bmobFile.download(savePath, new DownloadFileListener() {
            @Override
            public void onStart() {
                super.onStart();
                // 开始下载
            }

            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    //success
                    if (listener != null) {
                        listener.onDone(s);
                    }
                } else {
                    if (listener != null) {
                        listener.onError(e.getErrorCode(), e.getMessage());
                    }
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {
                if (listener != null) {
                    listener.onProgress(integer, l);
                }
            }
        });
    }


    /**
     *  上传单一文件的方法
     * @param filePath
     * @param listener
     */
    public static void uploadFile(String filePath, final OnProgressListener listener) {
        File file = new File(filePath);
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
                                 @Override
                                 public void done(BmobException e) {
                                     if (e == null) {
                                         //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                        if (listener != null){
                                            listener.onSuccess(bmobFile.getFileUrl());
                                        }
                                     } else {
                                         if (listener != null){
                                             listener.onFailure(e.getErrorCode(), e.getMessage());
                                         }
                                     }
                                 }

                                 @Override
                                 public void onProgress(Integer value) {
                                     super.onProgress(value);
                                     // 返回的上传进度（百分比）
                                    if (listener != null){
                                        listener.onProgress(value);
                                    }
                                 }
                             }
        );
    }

    /**
     *  更新云端用户信息
     * @param name
     * @param pwd
     * @param icon
     * @param level
     * @param group
     */
    public static void updateUser(String name, String pwd, String icon, int level, String group, final OnResponseListener listener){
        UserBmob newUsr = new UserBmob();
        if (!TextUtils.isEmpty(name)){
            newUsr.setUsername(name);
        }
        if (!TextUtils.isEmpty(pwd)){
            newUsr.setPassword(pwd);
        }
        if (!TextUtils.isEmpty(icon)){
            newUsr.setIcon(icon);
        }
        if (level != 0){
            newUsr.setLevel(level);
        }
        if (!TextUtils.isEmpty(group)){
            newUsr.setGroup(name);
        }
        UserBmob user = BmobUser.getCurrentUser(UserBmob.class);
        newUsr.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    if (listener != null){
                        listener.onSuccess(new String("update success"));
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
