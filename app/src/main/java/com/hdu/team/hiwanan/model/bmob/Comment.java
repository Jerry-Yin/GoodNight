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
     * integer	user		发表人 objectId
     * integer	like		点赞数
     * char	words   	    主评论语句
     * int 	lastid		    负数<0 : 没有@某人  |  正数>0 : @了某个人（id）的评论
     * ]
     */

    private int id;
    private String date;
    private String userId;
    private int like;
    private String words;
    private int lastId;


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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }
}
