package com.hdu.team.hiwanan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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


    /** Views*/
    private ImageView mBtnCancel;
    private ImageView mBtnDone;
    private TimePicker mTimePicker;
    private TextView mTvLastTime;
    private TextView mTvTitle;

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

    private List<Uri> ringtoneList = new ArrayList<>();
    RingtoneManager manager = new RingtoneManager(this);
    MediaPlayer preview = new MediaPlayer();

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
        mTvTitle = (TextView) findViewById(R.id.text_title);
        mTimePicker = (TimePicker) findViewById(R.id.time_picker);
        mTimePicker.setOnTimeChangedListener(this);
        mTimePicker.setIs24HourView(true);      //设置制式为24格式
        mTvLastTime = (TextView) findViewById(R.id.text_last_time);

        //获取前面传递过来的值
        mIntent = getIntent();
        mItemPosition = mIntent.getIntExtra("ItemPosition", 0);
        mTvTitle.setText(mIntent.getStringExtra("category"));
        mBtnCancel.setImageResource(R.drawable.ic_clea);
        mBtnDone.setImageResource(R.drawable.ic_done);
        //TODO:find the spinner from layout.
        mRingtone = (Spinner)findViewById(R.id.ringtone);
        mCategory = (Spinner)findViewById(R.id.category);

        List titleList = new ArrayList();


        Cursor cursor = manager.getCursor();
        //TODO:获取系统所有铃声列表
        while(cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            titleList.add(title);
            Uri ringtoneUri = manager.getRingtoneUri(cursor.getPosition());
            ringtoneList.add(ringtoneUri);
        }

        ArrayAdapter categoryAdapter= ArrayAdapter.createFromResource(this, R.array.category_values, R.layout.hi_spinner_item);
        categoryAdapter.setDropDownViewResource(R.layout.hi_spinner_dropdown);

        ArrayAdapter ringtoneAdapter = new ArrayAdapter(this,R.layout.hi_spinner_item, titleList);
        ringtoneAdapter.setDropDownViewResource(R.layout.hi_spinner_dropdown);

        mCategory.setAdapter(categoryAdapter);
        mCategory.setSelection(0,true);

        mRingtone.setAdapter(ringtoneAdapter);
        mRingtone.setSelection(0,true);

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
                SharedPreferences.Editor editor = getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, MODE_PRIVATE).edit();
                editor.putLong(HiConfig.DIF_HOURS + mItemPosition, mTimePicker.getCurrentHour());
                editor.putLong(HiConfig.DIF_MINUTES + mItemPosition, mTimePicker.getCurrentMinute());
                editor.commit();

                Intent intent = new Intent();
                intent.putExtra("hour", mTimePicker.getCurrentHour());
                intent.putExtra("minute", mTimePicker.getCurrentMinute());
                setResult(RESULT_OK, intent);

                HiToast.showToast(HiTimePickerActivity.this, "已将" + mIntent.getStringExtra("category") + "设定为从现在起 " + mHours + " 小时 " + mMinutes + " 分钟后");
                this.finish();
                break;

            default:
                break;
        }
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
              Uri uri = manager.getRingtoneUri((int)id);

              try {
                  if (preview.isPlaying()) {
                      preview.stop();
                      preview.reset();
                     // preview.release();
                      preview.setLooping(false);
                      preview.setDataSource(this,uri);
                      preview.prepare();
                      preview.start();

                  } else {
                      preview.setDataSource(this,uri);
                      preview.prepare();
                      preview.setLooping(false);
                      preview.start();
                      Log.d("duration", "" + preview.getDuration());

                  }

              } catch (IOException e) {
                  e.printStackTrace();
              }

              Log.d("selected ringtone", "" + id);
              break;
          case R.id.category:
              mCategoryId = id;
              Log.d("selected category", "" + id);
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
        super.onDestroy();
    }
}
