package com.hdu.team.hiwanan.model;

import android.util.Log;

import java.lang.reflect.Field;

import cn.bmob.v3.BmobUser;

/**
 * Created by JerryYin on 7/13/16.
 * 继承自BmobUser 的用户信息，里面已经封装了一部分信息
 */
public class UserBmob extends BmobUser {

    private static final String TAG = "UserBmob";
    //自定义信息
    private String sex;
    private String icon;
    private Integer level;
    private String group;


    public String getPassword(UserBmob user) {
        Log.d(TAG, "user = "+user);
        Log.d(TAG, "name = "+user.getUsername());

        Class c = user.getClass().getSuperclass();
        Field field = null;
        String pwd = null;
        try {
            field = c.getDeclaredField("password");
            field.setAccessible(true);
            pwd = (String) field.get(user);

            Log.d(TAG, "c = "+c);
            Log.d(TAG, "field = "+field);
//            Log.d(TAG, "field.byte = "+field.getByte(user));     //java.lang.IllegalArgumentException: Not a primitive field:
//            Log.d(TAG, "field.char = "+field.getChar(user));
            Log.d(TAG, "field.type = "+field.getType());
            Log.d(TAG, "pwd = "+pwd);
            Log.d(TAG, "field.get(user) = "+field.get(this));

            Field f2 = c.getDeclaredField("username");
            f2.setAccessible(true);
            Log.d(TAG, "name = "+f2.get(user));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return pwd;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
