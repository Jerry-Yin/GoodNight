package com.hdu.team.hiwanan.model.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by JerryYin on 4/8/17.
 */

public class Calendar extends BmobObject {

    /**
     * Calendar{
     * integer id			id
     * char abstract		关键字
     * char str		     	名言
     * char origin 			出处
     * char author			作者
     * integer like		    点赞数
     * integer comment		评论数
     * integer share		分享数
     * }
     */

    private int id;
    private String abstracts;
    private String str;
    private String origin;
    private String author;
    private int like;
    private int comment;
    private int share;
    private String date;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

