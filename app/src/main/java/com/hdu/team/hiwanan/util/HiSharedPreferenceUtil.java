package com.hdu.team.hiwanan.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Map;

/**
 * Created by JerryYin on 7/15/16.
 * SharedPreference 数据库辅助类
 */
public class HiSharedPreferenceUtil {

    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor mEditor;


    /**
     * 存储数据
     *
     * @param context
     * @param dbName
     * @param key
     * @param value
     */
    public static void putOneDataToSP(Context context, String dbName, String key, String value) {
        mPreferences = context.getSharedPreferences(dbName, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }

    
    public static void putMapsToSP(Context context, String dbName, List<Map<String, Object>> maps){
//        mPreferences = context.getSharedPreferences(dbName, Context.MODE_PRIVATE);
//        mEditor = mPreferences.edit();
//        for (Map<String, Object> map: maps){
//            mEditor.putString(map.);
//        }
//        mEditor.commit();

    }

    /**
     * 获取数据
     *
     * @param context
     * @param dbName
     * @param key
     * @param defValue
     * @return
     */
    public static String getDataFromSP(Context context, String dbName, String key, String defValue) {
        mPreferences = context.getSharedPreferences(dbName, Context.MODE_PRIVATE);
        return mPreferences.getString(key, defValue);
    }


    /**
     * 清除数据
     *
     * @param c
     * @param dbName
     * @return
     */
    public static boolean clearLocalData(Context c, String dbName) {
        mPreferences = c.getSharedPreferences(dbName, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.clear();
        mEditor.commit();
        return true;
    }


    /**
     * 制空数据
     *
     * @param c
     * @param dbName
     * @param key
     */
    public static void setDataToNull(Context c, String dbName, String key) {
        mPreferences = c.getSharedPreferences(dbName, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putString(key, null);
        mEditor.commit();
    }
}
