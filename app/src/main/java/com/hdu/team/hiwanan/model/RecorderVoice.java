package com.hdu.team.hiwanan.model;

/**
 * Created by JerryYin on 7/18/16.
 * 记录录音voice条目的类
 */
public class RecorderVoice {
    public String filePath;    //文件路径
    public float time;         //时长
    public String date;        //录制日期

    public RecorderVoice(String filePath, float time, String date) {
        super();
        this.filePath = filePath;
        this.time = time;
        this.date = date;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
