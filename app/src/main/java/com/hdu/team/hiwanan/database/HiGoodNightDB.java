package com.hdu.team.hiwanan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hdu.team.hiwanan.model.HiAlarmTab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryYin on 3/31/17.
 * <p>
 * 增删查改
 * 直接调用的数据库工具类
 */

public class HiGoodNightDB {

    private static HiGoodNightDB mInstance;
    private SQLiteDatabase db;
    private HiDatabaseOpenHelper mOpenHelper;


    private HiGoodNightDB(Context context) {
        mOpenHelper = new HiDatabaseOpenHelper(context);
//        db = mOpenHelper.getWritableDatabase();
    }

    public static synchronized HiGoodNightDB getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new HiGoodNightDB(context);
        }
        return mInstance;
    }


    public boolean saveAlarmTab(HiAlarmTab tab) {
        boolean success = false;
        db = mOpenHelper.getWritableDatabase();
        if (tab != null) {
            ContentValues cv = new ContentValues();
            cv.put(HiAlarmTab.AlarmEntry.COLUMN_ID, tab.getId());
            cv.put(HiAlarmTab.AlarmEntry.COLUMN_ICON, tab.getIcon());
            cv.put(HiAlarmTab.AlarmEntry.COLUMN_CATEGORY, tab.getCategory());
            cv.put(HiAlarmTab.AlarmEntry.COLUMN_TIME, tab.getTime());
            cv.put(HiAlarmTab.AlarmEntry.COLUMN_SWITCH, tab.isOn());
            cv.put(HiAlarmTab.AlarmEntry.COLUMN_MUSIC, tab.getMusicId());
            db.insert(HiAlarmTab.AlarmEntry.TABLE_NAME, null, cv);
            success = true;
        }
        db.close();
        db = null;
        return success;
    }

    /**
     * delete by id
     *
     * @param id
     * @return
     */
    public int deleteAlarmTab(int id) {
        if (db == null)
            db = mOpenHelper.getWritableDatabase();
        int result = db.delete(HiAlarmTab.AlarmEntry.TABLE_NAME, HiAlarmTab.AlarmEntry.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        db = null;
        return result;

    }


    /**
     * query a alarmtab by id
     * @param id
     * @return
     */
    public HiAlarmTab queryAlarmTab(int id) {
        if (db == null)
            db = mOpenHelper.getReadableDatabase();
        HiAlarmTab tab = new HiAlarmTab();
//        String sql = "SELECT * FROM " + HiAlarmTab.AlarmEntry.TABLE_NAME +" WHERE " + HiAlarmTab.AlarmEntry.CLOUMN_ID + " = "+ id;
//        db.execSQL(sql);
        //Cursor cursor = db.rawQuery("SELECT * FROM "+ HiAlarmTab.AlarmEntry.TABLE_NAME + " WHERE " + HiAlarmTab.AlarmEntry.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        Cursor cursor = db.query(HiAlarmTab.AlarmEntry.TABLE_NAME,new String[]{HiAlarmTab.AlarmEntry.COLUMN_ID,HiAlarmTab.AlarmEntry.COLUMN_ICON,HiAlarmTab.AlarmEntry.COLUMN_CATEGORY,
                HiAlarmTab.AlarmEntry.COLUMN_TIME, HiAlarmTab.AlarmEntry.COLUMN_SWITCH,HiAlarmTab.AlarmEntry.COLUMN_MUSIC},
        HiAlarmTab.AlarmEntry.COLUMN_ID + "=?",new String[]{String.valueOf(id)}, null,null,null);
        int size = cursor.getCount();
        if (cursor.moveToFirst()){
            tab.setId(cursor.getInt(cursor.getColumnIndex(HiAlarmTab.AlarmEntry.COLUMN_ID)));
            tab.setIcon(cursor.getInt(cursor.getColumnIndex(HiAlarmTab.AlarmEntry.COLUMN_ICON)));
            tab.setCategory(cursor.getString(cursor.getColumnIndex(HiAlarmTab.AlarmEntry.COLUMN_CATEGORY)));
            tab.setTime(cursor.getString(cursor.getColumnIndex(HiAlarmTab.AlarmEntry.COLUMN_TIME)));
            tab.setOn(cursor.getInt(cursor.getColumnIndex(HiAlarmTab.AlarmEntry.COLUMN_SWITCH)) == 1 ? true : false);
            tab.setMusicId(cursor.getInt(cursor.getColumnIndex(HiAlarmTab.AlarmEntry.COLUMN_MUSIC)));
        }
        cursor.close();
        db.close();
        db = null;
        return tab;
    }

    /**
     * query all the tabs
     *
     * @return
     */
    public List<HiAlarmTab> queryAllAlarmTabs() {
        if (db == null)
            db = mOpenHelper.getReadableDatabase();
        List<HiAlarmTab> alarmTabs = new ArrayList<>();
        Cursor cursor = db.query(HiAlarmTab.AlarmEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                HiAlarmTab tab = new HiAlarmTab();
                tab.setId(cursor.getInt(cursor.getColumnIndex(HiAlarmTab.AlarmEntry.COLUMN_ID)));
                tab.setIcon(cursor.getInt(cursor.getColumnIndex(HiAlarmTab.AlarmEntry.COLUMN_ICON)));
                tab.setCategory(cursor.getString(cursor.getColumnIndex(HiAlarmTab.AlarmEntry.COLUMN_CATEGORY)));
                tab.setTime(cursor.getString(cursor.getColumnIndex(HiAlarmTab.AlarmEntry.COLUMN_TIME)));
                tab.setOn(cursor.getInt(cursor.getColumnIndex(HiAlarmTab.AlarmEntry.COLUMN_SWITCH)) == 1 ? true : false);
                tab.setMusicId(cursor.getInt(cursor.getColumnIndex(HiAlarmTab.AlarmEntry.COLUMN_MUSIC)));
                alarmTabs.add(tab);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        db = null;
        return alarmTabs;
    }

    /**
     * update one alarmTab
     * @param tab
     * @return
     */
    public boolean updateAlarmTab(HiAlarmTab tab){
        if (tab == null)
            return false;
        int id = tab.getId();
        if (db == null)
            db = mOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(HiAlarmTab.AlarmEntry.COLUMN_ID, id);
        cv.put(HiAlarmTab.AlarmEntry.COLUMN_ICON, tab.getIcon());
        cv.put(HiAlarmTab.AlarmEntry.COLUMN_CATEGORY, tab.getCategory());
        cv.put(HiAlarmTab.AlarmEntry.COLUMN_TIME, tab.getTime());
        cv.put(HiAlarmTab.AlarmEntry.COLUMN_SWITCH, tab.isOn() ? 1 : 0);
        cv.put(HiAlarmTab.AlarmEntry.COLUMN_MUSIC, tab.getMusicId());
        db.update(HiAlarmTab.AlarmEntry.TABLE_NAME, cv, HiAlarmTab.AlarmEntry.COLUMN_ID + "= ?", new String[]{String.valueOf(id)});
        db.close();
        db = null;
        return true;
    }

    /**
     * retrieve the db size
     * @return size
     */
    public int getDbSize() {
        if (db == null)
            db = mOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(HiAlarmTab.AlarmEntry.TABLE_NAME,null, null, null, null, null, null);
        int size = cursor.getCount();
        db.close();
        db = null;
        return size;
    }
}
