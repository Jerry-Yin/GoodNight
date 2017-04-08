package com.hdu.team.hiwanan.network;

import android.content.Context;
import android.text.TextUtils;

import com.hdu.team.hiwanan.listener.OnProgressListener;
import com.hdu.team.hiwanan.listener.OnResponseListener;
import com.hdu.team.hiwanan.model.bmob.Calendar;
import com.hdu.team.hiwanan.model.bmob.User;
import com.hdu.team.hiwanan.model.bmob.UserBmob;
import com.hdu.team.hiwanan.listener.OnDownloadListener;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by JerryYin on 7/13/16.
 * 采用 Bmob 方式的网络请求工具类
 */
public class BmobNetworkUtils {

    /** ---------------------------------------- Bmob云服务器 “用户信息” 数据操作模块 --------------------------------*/
    /**
     * 用户注册
     *
     * @param user
     * @param listener
     */
    public static void signUp(final User user, final OnResponseListener listener) {
        if (user != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
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
            }).start();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();
    }


    public static void signIn() {

    }

    /**
     * 用户登录方式2
     *
     * @param user
     * @param listener
     */
    public static void signIn(final UserBmob user, final OnResponseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();
    }


    /**
     * 下载文件存储到本地的方法
     *
     * @param context
     * @param bmobFile
     * @param listener
     */
    public static void downloadIcon(final Context context, final BmobFile bmobFile, final OnDownloadListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();
    }


    /**
     * 上传单一文件的方法
     *
     * @param filePath
     * @param listener
     */
    public static void uploadFile(final String filePath, final OnProgressListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(filePath);
                final BmobFile bmobFile = new BmobFile(file);
                bmobFile.uploadblock(new UploadFileListener() {
                                         @Override
                                         public void done(BmobException e) {
                                             if (e == null) {
                                                 //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                                 if (listener != null) {
                                                     listener.onSuccess(bmobFile.getFileUrl());
                                                 }
                                             } else {
                                                 if (listener != null) {
                                                     listener.onFailure(e.getErrorCode(), e.getMessage());
                                                 }
                                             }
                                         }

                                         @Override
                                         public void onProgress(Integer value) {
                                             super.onProgress(value);
                                             // 返回的上传进度（百分比）
                                             if (listener != null) {
                                                 listener.onProgress(value);
                                             }
                                         }
                                     }
                );
            }
        }).start();
    }

    /**
     * 更新云端用户信息
     *
     * @param name
     * @param pwd
     * @param icon
     * @param level
     * @param group
     */
    public static void updateUser(String name, String pwd, String icon, int level, String group, final OnResponseListener listener) {
        final UserBmob newUsr = new UserBmob();
        if (!TextUtils.isEmpty(name)) {
            newUsr.setUsername(name);
        }
        if (!TextUtils.isEmpty(pwd)) {
            newUsr.setPassword(pwd);
        }
        if (!TextUtils.isEmpty(icon)) {
            newUsr.setIcon(icon);
        }
        if (level != 0) {
            newUsr.setLevel(level);
        }
        if (!TextUtils.isEmpty(group)) {
            newUsr.setGroup(name);
        }
        final UserBmob user = BmobUser.getCurrentUser(UserBmob.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                newUsr.update(user.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            if (listener != null) {
                                listener.onSuccess(new String("update success"));
                            }
                        } else {
                            if (listener != null) {
                                listener.onFailure(e.getErrorCode(), e.getMessage());
                            }
                        }
                    }
                });
            }
        }).start();
    }


    /**
     * ---------------------------------------- Bmob云服务器 “日历 & 评论” 数据操作模块 --------------------------------
     */

    /**
     * 上传
     * @param calendar
     * @param listener
     * @return
     */
    public static boolean uploadCalendar(final Calendar calendar, final OnResponseListener listener) {
        if (calendar == null)
            return false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                calendar.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            if (listener != null) {
                                listener.onSuccess(s);
                            }
                        } else {
                            if (listener != null)
                                listener.onFailure(e.getErrorCode(), e.getMessage());
                        }
                    }
                });
            }
        }).start();
        return true;
    }

    /**
     * 根据日期来获取当前日历的信息
     * "yyyy-MM-dd"
     *
     * @param date
     */
    public static void queryCalendar(final String date, final OnResponseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<Calendar> query = new BmobQuery<>();
                query.addWhereEqualTo("date", date);
                query.findObjects(new FindListener<Calendar>() {
                    @Override
                    public void done(List<Calendar> list, BmobException e) {
                        if (e == null) {
                            if (listener != null) {
                                listener.onSuccess(list);
                            }
                        } else {
                            if (listener != null)
                                listener.onFailure(e.getErrorCode(), e.getMessage());
                        }
                    }
                });
            }
        }).start();
    }
}
