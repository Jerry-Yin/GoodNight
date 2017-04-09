package com.hdu.team.hiwanan.model.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by JerryYin on 4/8/17.
 */

public class Comment extends BmobObject {

    /**
     * Summary[
     * integer	id 			数量id（唯一标志符）
     * char	date		    日期
     * integer	user		发表人名字 objectId
     * integer	like		点赞数
     * char	words   	    主评论语句
     * int 	lastid		    负数<0 : 没有@某人  |  正数>0 : @了某个人（那条评论的id）的评论
     * String lastName      被@的用户名
     * ]
     */

    private int id;
    private String userid;
    private String user;
    private String date;    //yyyy-mm-dd
    private String time;    //hh:mm:ss(只需要显示 hh:mm)
    private int like;
    private String words;
    private int lastid;
    private String lastname;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserid() {
        return userid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public int getLastid() {
        return lastid;
    }

    public void setLastid(int lastid) {
        this.lastid = lastid;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }
}
