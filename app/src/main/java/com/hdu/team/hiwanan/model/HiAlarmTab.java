package com.hdu.team.hiwanan.model;

import android.content.Context;
import android.provider.BaseColumns;
import android.widget.Switch;

/**
 * Created by JerryYin on 3/31/17.
 *
 * TimeTab  的表结构 （表名 与 列名）
 * 通过实现 BaseColumns 的接口，内部类可以继承到一个名为_ID的主键，
 * 这个对于Android里面的一些类似cursor adaptor类是很有必要的。
 * 这么做不是必须的，但这样能够使得我们的DB与Android的framework能够很好的相容。
 */

public class HiAlarmTab {

    public final static String CATEGORY_READY = "预备时间";
    public final static String CATEGORY_SLEEP = "入睡时间";
    public final static String CATEGORY_GETUP = "早起时间";

    /**
     * key of the map value
     */
    public final static String ICON = "icon";
    public final static String CATEGORY = "category";
    public final static String TIME = "time";
    public final static String SWITCH = "switch";
    public final static String MUSIC = "music";


    private int id;     //alarm id = position
    private int icon;   //R.drawable.icon
    private String category;
    private String time;
    private boolean on;
    private int musicId;

    public HiAlarmTab() {
    }
    public HiAlarmTab(int id, int icon, String category, String time, boolean on, int musicId) {
        this.id = id;
        this.icon = icon;
        this.category = category;
        this.time = time;
        this.on = on;
        this.musicId = musicId;
    }

    /**
     * Alarm  闹钟 表单创建
     * "_ID"
     * "icon"       integer
     * "category"   text
     * "time"       text
     * "switch"     integer （1-true  0-false）
     * "music"      integer  (R.raw.music)
     */
    public static abstract class AlarmEntry implements BaseColumns{
        public static final String BLANK = " ";
        public static final String TABLE_NAME = "alarm";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_CATEGORY= "category";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_SWITCH = "switch";
        public static final String COLUMN_MUSIC = "music";
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }





}
