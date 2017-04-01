package com.hdu.team.hiwanan.manager;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.model.HiAlarmTab;

import java.util.HashMap;
import java.util.Map;

import static com.hdu.team.hiwanan.model.HiAlarmTab.CATEGORY;
import static com.hdu.team.hiwanan.model.HiAlarmTab.ICON;
import static com.hdu.team.hiwanan.model.HiAlarmTab.SWITCH;
import static com.hdu.team.hiwanan.model.HiAlarmTab.TIME;
import static com.hdu.team.hiwanan.model.HiAlarmTab.MUSIC;

/**
 * Created by JerryYin on 11/23/15.
 * 首页时间表tab管理类
 */
public class HiAlarmTabManager {


    private String mTime;


    public String getTime() {
        return mTime;
    }

    public void putTime(String time) {
        this.mTime = time;
    }


    /**
     * 创建首页时间tab的方法
     *
     * @param icon     指定的图标资源id（目前默认为 R.drawable.ic_access_alarm_black_24dp ）
     * @param category 时间种类3种
     * @param time     具体设定时间
     * @return
     */
    public static HiAlarmTab createMapTab(int id, int icon, String category, String time) {
        return createMapTab(id, icon, category, time, false, R.raw.voice);
    }

    /**
     * all most the same as the above one, but added another argument.
     *
     * @param icon         image source id
     * @param category     clock category
     * @param time         time for clock
     * @param switchStatus switch status setting when new it.
     * @param musicId      music for alarm clock  (default R.raw.voice)
     * @return HiAlarmTab
     */
    public static HiAlarmTab createMapTab(int id, int icon, String category, String time, boolean switchStatus, int musicId) {
        if (musicId == 0)
            musicId = R.raw.voice;
        HiAlarmTab alarmTab = new HiAlarmTab(id, icon, category, time, switchStatus, musicId);
        return alarmTab;
    }


    public static Boolean saveMapTab(Map<String, Object> map) {
        int icon = (int) map.get(ICON);
        String category = (String) map.get(CATEGORY);
        String time = (String) map.get(TIME);


        return false;
    }

}