package com.hdu.team.hiwanan.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by JerryYin on 7/13/16.
 */
public class User extends BmobObject{

    public static String usrName;
    public static String password;
    public static String sex;
    public static Integer phoneNumber;
    public static String email;
    public static String age;

    public User(String usrName, String password, String sex) {
        this.usrName = usrName;
        this.password = password;
        this.sex =sex;
    }
}
