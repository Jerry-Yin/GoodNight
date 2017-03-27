package com.hdu.team.hiwanan.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JerryYin on 11/23/15.
 * 首页时间表tab管理类
 */
public class HiTimeTabManager {

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

    private String mTime;


//    private ImageView mIcon;
//    private TextView mTimeCategory;
//    private TextView mTime;
//    private Switch mSwitch;
//
//
//
//    public HiTimeTabManager(){
//
//    }


    public String getTime() {
        return mTime;
    }

    public void putTime(String time){
        this.mTime = time;
    }

    /**
     * 创建首页时间tab的方法
     * @param imgId         指定的图标资源id（目前默认为 R.drawable.ic_access_alarm_black_24dp ）
     * @param tvCategory    时间种类3种
     * @param time          具体设定时间
     * @param context       用于new一个Switch
     * @return
     */
    public static Map<String, Object> createMapTab(int imgId, String tvCategory, String time, Context context) {
        Map<String, Object> map = new HashMap<>();
        map.put(ICON, imgId);
        map.put(CATEGORY, tvCategory);
        map.put(TIME, time);
        Switch aSwitch = new Switch(context);
        map.put(SWITCH, aSwitch);
        return map;
    }

    /**
     * all most the same as the above one, but added another argument.
     * @param imgId image source id
     * @param tvCategory clock category
     * @param time time for clock
     * @param context used for new switch
     * @param switchStatus switch status setting when new it.
     * @return Map<String, Object> map
     */
    public static Map<String, Object> createMapTab(int imgId, String tvCategory, String time, Context context, boolean switchStatus){
        Map<String, Object> map = new HashMap<>();
        map.put(ICON, imgId);
        map.put(CATEGORY, tvCategory);
        map.put(TIME, time);
        Switch aSwitch = new Switch(context);
        aSwitch.setChecked(switchStatus);
        map.put(SWITCH, aSwitch);
        return map;
    }



    public static Map<String, Object> createMapTab(int imgId, String tvCategory, String time) {
        Map<String, Object> map = new HashMap<>();
        map.put(ICON, imgId);
        map.put(CATEGORY, tvCategory);
        map.put(TIME, time);
        return map;
    }

    public static Boolean saveMapTab(Map<String, Object> map){
        int imgId = (int) map.get(ICON);
        String category = (String) map.get(CATEGORY);
        String time = (String) map.get(TIME);



        return false;
    }

}