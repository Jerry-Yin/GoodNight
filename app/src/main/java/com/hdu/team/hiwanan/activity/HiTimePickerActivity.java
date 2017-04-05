package com.hdu.team.hiwanan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.base.HiActivity;
import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.database.HiGoodNightDB;
import com.hdu.team.hiwanan.model.HiAlarmTab;
import com.hdu.team.hiwanan.util.HiLog;
import com.hdu.team.hiwanan.util.HiToast;
import com.hdu.team.hiwanan.util.common.HiTimesUtil;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by JerryYin on 11/23/15.
 */
public class HiTimePickerActivity extends HiActivity implements TimePicker.OnTimeChangedListener, AdapterView.OnItemSelectedListener{


    private  static final String TAG = "HiTimePickerActivity";
    /** Views*/
    private ImageView mBtnCancel;
    private ImageView mBtnDone;
    private TimePicker mTimePicker;
    private TextView mTvLastTime;
    //private TextView mTvTitle;

    //TODO:new view for spinner category and ringtone, just for testing
    private Spinner mRingtone;
    private Spinner mCategory;

    /** Values*/
    private Intent mIntent;
    private int mItemPosition;  //前面点击的Item索引

    private long mHours = 00;       //临时变量，用于记录时差
    private long mMinutes = 00;

    private long mRingtoneId = 0; //用于存放铃声的id
    private long mCategoryId = 0; //用于存放铃声种类的id

    HiGoodNightDB mDbManager = HiGoodNightDB.getInstance(this);

    RingtoneManager mRingtoneManager = new RingtoneManager(this);//ringtone manager 用户获取系统默认的铃声列表
    MediaPlayer mPreviewPlayer = new MediaPlayer();//用于铃声选择时播放试听

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_time_picker);

        setupViews();

    }

    private void setupViews() {
        mBtnCancel = (ImageView) findViewById(R.id.btn_title_back);
        mBtnDone = (ImageView) findViewById(R.id.btn_title_next);
        mBtnCancel.setOnClickListener(this);
        mBtnDone.setOnClickListener(this);
        //mTvTitle = (TextView) findViewById(R.id.text_title);
        mTimePicker = (TimePicker) findViewById(R.id.time_picker);
        mTimePicker.setOnTimeChangedListener(this);
        mTimePicker.setIs24HourView(true);      //设置制式为24格式
        mTvLastTime = (TextView) findViewById(R.id.text_last_time);

        mRingtone = (Spinner)findViewById(R.id.ringtone);
        mCategory = (Spinner)findViewById(R.id.category);

        //获取前面传递过来的值
        mIntent = getIntent();


        //mItemPosition = mIntent.getIntExtra("ItemPosition", 0);
        //mTvTitle.setText(mIntent.getStringExtra("category"));
        mBtnCancel.setImageResource(R.drawable.ic_clea);
        mBtnDone.setImageResource(R.drawable.ic_done);
        //TODO:find the spinner from layout.


        List titleList = new ArrayList();


        Cursor cursor = mRingtoneManager.getCursor();
        //TODO:获取系统所有铃声列表
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
                titleList.add(title);
                //Uri ringtoneUri = manager.getRingtoneUri(cursor.getPosition());
                //ringtoneList.add(ringtoneUri);
            }
        }

        ArrayAdapter categoryAdapter= ArrayAdapter.createFromResource(this, R.array.category_values, R.layout.hi_spinner_item);
        categoryAdapter.setDropDownViewResource(R.layout.hi_spinner_dropdown);

        ArrayAdapter ringtoneAdapter = new ArrayAdapter(this,R.layout.hi_spinner_item, titleList);
        ringtoneAdapter.setDropDownViewResource(R.layout.hi_spinner_dropdown);

        mCategory.setAdapter(categoryAdapter);


        mRingtone.setAdapter(ringtoneAdapter);

        if(mIntent.getExtras().getInt(HiConfig.REQUEST_TYPE, 0) == HiConfig.MODIFY_REQUEST){
         // if it is the modify request, load the data which user set before.
         long id = mIntent.getExtras().getLong("id", 0);
         HiAlarmTab tab = mDbManager.queryAlarmTab((int) id);

         if(Build.VERSION.SDK_INT > 24) {
         // the time picker set hour / minute is supported only since android 24.
         String time = tab.getTime();
         int hour = Integer.parseInt(time.split(":")[0].trim());
         int minute = Integer.parseInt(time.split(":")[1].trim());
         mTimePicker.setHour(hour);
         mTimePicker.setMinute(minute);
         }
         String category = tab.getCategory();
         if("预备时间".equalsIgnoreCase(category)) {
             mCategoryId = 0;
         } else if("入睡时间".equalsIgnoreCase(category)) {
             mCategoryId = 1;
         } else if("起床时间".equalsIgnoreCase(category)) {
             mCategoryId = 2;
         }
         mCategory.setSelection((int) mCategoryId, true);
         mRingtoneId = tab.getMusicId();
         mRingtone.setSelection((int) mRingtoneId, true);

         } else {
            mCategory.setSelection(0, true);
            mRingtone.setSelection(0, true);
        }

        mCategory.setOnItemSelectedListener(this);
        mRingtone.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_title_back:
                //取消选择
                this.finish();
                break;

            case R.id.btn_title_next:
                /**
                 * TODO
                 * 1.确定选择；
                 * 2.保存时间差到数据库，开始计时；
                 * 3.返回设定的时间给主界面设置界面时间;
                 * 4.确定铃声
                 */
//                SharedPreferences.Editor editor = getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, MODE_PRIVATE).edit();
//                editor.putLong(HiConfig.DIF_HOURS + mItemPosition, mTimePicker.getCurrentHour());
//                editor.putLong(HiConfig.DIF_MINUTES + mItemPosition, mTimePicker.getCurrentMinute());
//                editor.commit();
//
//                Intent intent = new Intent();
//                intent.putExtra("hour", mTimePicker.getCurrentHour());
//                intent.putExtra("minute", mTimePicker.getCurrentMinute());

                //TODO: here we transfer to saving alarm info to db.
                //int id, int icon, String category, String time, boolean switch status, int music id;

                int requestCode = mIntent.getExtras().getInt(HiConfig.REQUEST_TYPE, 0);
                switch(requestCode) {
                    case HiConfig.CREATE_REQUEST :
                        addAlarmClock();
                        break;
                    case HiConfig.MODIFY_REQUEST:
                        long id = mIntent.getExtras().getLong("id", 0);
                        //load the data which user set before.

                        modifyAlarmClock((int)id);
                        break;
                    default:
                        HiToast.showToast(HiTimePickerActivity.this, "Opps, Something happens!");
                        break;
                }



                break;

            default:
                break;
        }
    }


    private void addAlarmClock() {
        // the id of the new alarm clock is db size + 1;
        int id = mDbManager.getDbSize();
        int icon = R.drawable.ic_access_alarm_black_24dp;
        String category = getResources().getStringArray(R.array.category_values)[(int) mCategoryId];
        String hour = mTimePicker.getCurrentHour() < 10 ? "0" + mTimePicker.getCurrentHour(): String.valueOf(mTimePicker.getCurrentHour());
        String minute = mTimePicker.getCurrentMinute() < 10 ? "0" + mTimePicker.getCurrentMinute(): String.valueOf(mTimePicker.getCurrentMinute());
        String time = hour + ":" +minute;
        boolean on = false;//default true;
        int musicId = (int) mRingtoneId;
        HiAlarmTab alarmTab = new HiAlarmTab(id, icon, category, time, on, musicId);
        mDbManager.saveAlarmTab(alarmTab);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);

//                HiToast.showToast(HiTimePickerActivity.this, "已将" + mIntent.getStringExtra("category") + "设定为从现在起 " + mHours + " 小时 " + mMinutes + " 分钟后");
        HiToast.showToast(HiTimePickerActivity.this, "已将" + category + "设置为" + time);
        this.finish();
    }

    private void modifyAlarmClock(int id){
        HiAlarmTab alarmTab = mDbManager.queryAlarmTab(id);
        String category = getResources().getStringArray(R.array.category_values)[(int) mCategoryId];
        alarmTab.setCategory(category);
        String hour = mTimePicker.getCurrentHour() < 10 ? "0" + mTimePicker.getCurrentHour(): String.valueOf(mTimePicker.getCurrentHour());
        String minute = mTimePicker.getCurrentMinute() < 10 ? "0" + mTimePicker.getCurrentMinute(): String.valueOf(mTimePicker.getCurrentMinute());
        String time = hour + ":" +minute;
        alarmTab.setTime(time);
        alarmTab.setMusicId((int) mRingtoneId);
        //TODO:to force trigger it by user, we set default status is false.
        alarmTab.setOn(false);
        HiLog.d(TAG, "music id" + alarmTab.getMusicId());
        mDbManager.updateAlarmTab(alarmTab);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        HiToast.showToast(HiTimePickerActivity.this,"已将" + category + "修改为" + time);
        this.finish();
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        //TODO 选择时间，根据系统当前时间计算时差，改变上方剩余时间文字，并注意回传给主界面
        Bundle bundle = HiTimesUtil.getTimeDif(hourOfDay, minute);
//        String days = bundle.get("days").toString();
        mHours = bundle.getLong("hours");
        mMinutes = bundle.getLong("minutes");
        String text = "还有 "+mHours+" 小时 "+mMinutes+" 分钟";
        mTvLastTime.setText(text);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      switch (parent.getId()) {
          case R.id.ringtone:
              mRingtoneId = id;
              Uri uri = mRingtoneManager.getRingtoneUri((int)id);

              try {
                  if (mPreviewPlayer.isPlaying()) {
                      mPreviewPlayer.stop();
                      mPreviewPlayer.reset();
                     // preview.release();
                      mPreviewPlayer.setLooping(false);
                      mPreviewPlayer.setDataSource(this,uri);
                      mPreviewPlayer.prepare();
                      mPreviewPlayer.start();
                      mPreviewPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                          @Override
                          public void onCompletion(MediaPlayer mp) {
                              mp.stop();
                          }
                      });
                      HiLog.d("isLooping", "" + mPreviewPlayer.isLooping());
                  } else {
                      mPreviewPlayer.reset();
                      mPreviewPlayer.setDataSource(this,uri);
                      mPreviewPlayer.prepare();
                      mPreviewPlayer.setLooping(false);
                      mPreviewPlayer.start();
                      mPreviewPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                          @Override
                          public void onCompletion(MediaPlayer mp) {
                              mp.stop();
                          }
                      });
                      HiLog.d("isLooping", "" + mPreviewPlayer.isLooping());
                  }

              } catch (IOException e) {
                  e.printStackTrace();
              }
              break;
          case R.id.category:
              mCategoryId = id;
              break;
          default:
              break;
      }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onDestroy() {
        if (mPreviewPlayer !=null) {
            mPreviewPlayer.release();
        }
        super.onDestroy();
    }
}
