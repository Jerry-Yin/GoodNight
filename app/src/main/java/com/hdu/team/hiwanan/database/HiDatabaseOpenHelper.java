package com.hdu.team.hiwanan.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hdu.team.hiwanan.model.HiAlarmTab;

/**
 * Created by JerryYin on 3/30/17.
 */

public class HiDatabaseOpenHelper extends SQLiteOpenHelper{


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HiGoodNight.db";

    private static final String TEXT_TYPE = "TEXT";
    private static final String INTEGER_TYPE = "INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String CREATE_ALARM = "CREATE TABLE "+ HiAlarmTab.AlarmEntry.TABLE_NAME + "( " +
            HiAlarmTab.AlarmEntry._ID + HiAlarmTab.AlarmEntry.BLANK +"INTEGER PRIMARY KEY AUTOINCREMENT," +
            HiAlarmTab.AlarmEntry.COLUMN_ID + HiAlarmTab.AlarmEntry.BLANK + INTEGER_TYPE + COMMA_SEP +
            HiAlarmTab.AlarmEntry.COLUMN_ICON + HiAlarmTab.AlarmEntry.BLANK + INTEGER_TYPE + COMMA_SEP +
            HiAlarmTab.AlarmEntry.COLUMN_CATEGORY + HiAlarmTab.AlarmEntry.BLANK + TEXT_TYPE + COMMA_SEP +
            HiAlarmTab.AlarmEntry.COLUMN_TIME + HiAlarmTab.AlarmEntry.BLANK + TEXT_TYPE + COMMA_SEP +
            HiAlarmTab.AlarmEntry.COLUMN_SWITCH + HiAlarmTab.AlarmEntry.BLANK + INTEGER_TYPE + COMMA_SEP +
            HiAlarmTab.AlarmEntry.COLUMN_MUSIC + HiAlarmTab.AlarmEntry.BLANK + INTEGER_TYPE + " )";

    private static final String DELETE_ALARM = "DROP TABLE IF EXISTS "+ HiAlarmTab.AlarmEntry.TABLE_NAME;


    public HiDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ALARM);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_ALARM);
        onCreate(db);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
