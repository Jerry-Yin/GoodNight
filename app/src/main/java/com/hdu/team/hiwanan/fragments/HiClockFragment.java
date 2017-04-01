package com.hdu.team.hiwanan.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.activity.HiTimePickerActivity;
import com.hdu.team.hiwanan.broadcast.HiAlarmClockReceiver;
import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.model.HiAlarmTab;
import com.hdu.team.hiwanan.util.HiLog;
import com.hdu.team.hiwanan.manager.HiAlarmTabManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiClockFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "HiClockFragment";
;
    private static final int FLAG_REQUEST_CODE = 001;
    private static final String CREATE_CLOCK = "请选择闹钟时间类型";
    private CharSequence[] clock_category = new CharSequence[]{"预备时间", "入睡时间", "早起时间"};
    /**
     * Constants
     */
    private View mContentView;
    private Activity mSelf;

    /**
     * View
     */
//    private ImageButton mbtnAddClock;
    private TextView mTvSleepTime;
    private ListView mListView;
    private FloatingActionButton mbtnAddClock;

    /**
     * Values
     */
    private BaseAdapter mTimeListAdapter;
    private List<HiAlarmTab> mAlarmList;
    //TODO:kaikai added list value for switch status.
    private List<Boolean> mSwitchStatusList;

    private int mSelItemPosition;   //当前选中的item位置

    private SharedPreferences mPreferences;

    public Activity getmSelf() {
        return mSelf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mContentView) {
            ViewGroup vg = (ViewGroup) mContentView.getParent();
            if (null != vg) {
                vg.removeView(mContentView);
            }
        } else {
            mContentView = inflater.inflate(R.layout.layout_clock, container, false);
            mSelf = getActivity();
            setupViews();
            initTime();
        }
        return mContentView;
    }

    private void setupViews() {
        mbtnAddClock = (FloatingActionButton) mContentView.findViewById(R.id.btn_add_clock);
        mbtnAddClock.setOnClickListener(this);

//        mTvSleepTime = (TextView) mContentView.findViewById(R.id.text_ready_time_point);
//        mTvSleepTime.setOnClickListener(this);

        mListView = (ListView) mContentView.findViewById(R.id.list_sleep_time);
        mAlarmList = getFirstLists();
        //TODO: kaikai added for initial switch flags
        mSwitchStatusList = getFirstSwitchStatusList();


        mTimeListAdapter = new HiTimeListAdapter(mSelf, mAlarmList);
        mListView.setAdapter(mTimeListAdapter);
//        mTimeListAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initTime();
        mTimeListAdapter.notifyDataSetChanged();
    }


    /**
     * todo
     * 界面初始化，进入界面从sp数据库中判断是否有已经设定好的时间tab数，如果有，就拿出来设置；没有就默认；
     * 判断时需要判断多个tab item 的数据，并一次设定界面
     * 遍历mTimeList，
     */
    private void initTime() {
        mPreferences = mSelf.getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, Context.MODE_PRIVATE);

        if (mAlarmList.size() != 0) {
            for (int i = 0; i < mAlarmList.size(); i++) {
                long hour = mPreferences.getLong(HiConfig.DIF_HOURS + i, 00);
                long minute = mPreferences.getLong(HiConfig.DIF_MINUTES + i, 00);
                String time = hour + " : " + minute;
                mAlarmList.get(i).setTime(time);
            }
        }
        mTimeListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_clock:
                // todo 添加闹钟（类型，时间）
                AlertDialog.Builder builder = new AlertDialog.Builder(mSelf);
                builder.setTitle(CREATE_CLOCK)
                .setItems(clock_category, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HiAlarmTab map = HiAlarmTabManager.createMapTab(mAlarmList.size(), R.drawable.ic_access_alarm_black_24dp, (String) clock_category[which], "00 : 00", false, 0);
                        mAlarmList.add(map);
                        mTimeListAdapter.notifyDataSetChanged();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTimeListAdapter.notifyDataSetChanged();
                    }
                })
                .create().show();

                break;

            case R.id.text_ready_time_point:

                break;
            default:
                break;
        }
    }

    /**
     * 初次加载三条时间表
     * 每一个map表示一条tab
     */
    public List<HiAlarmTab> getFirstLists() {
        List<HiAlarmTab> firstLists = new ArrayList<>();
        //TODO 添加三条初始化的tab
        HiAlarmTab tab1 = HiAlarmTabManager.createMapTab(0, R.drawable.ic_access_alarm_black_24dp, HiAlarmTab.CATEGORY_READY, "10 : 00");
        HiAlarmTab tab2 = HiAlarmTabManager.createMapTab(1, R.drawable.ic_access_alarm_black_24dp, HiAlarmTab.CATEGORY_READY, "10 : 30");
        HiAlarmTab tab3 = HiAlarmTabManager.createMapTab(2, R.drawable.ic_access_alarm_black_24dp, HiAlarmTab.CATEGORY_READY, "08 : 00");
        firstLists.add(tab1);
        firstLists.add(tab2);
        firstLists.add(tab3);
        return firstLists;
    }

    public List<Boolean> getFirstSwitchStatusList(){
        //TODO: init 3 defalut switch flags
        ArrayList<Boolean> firstLists= new ArrayList<Boolean>();
        mPreferences = mSelf.getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, Context.MODE_PRIVATE);
        int size = mPreferences.getInt(HiConfig.STATUS_NUM, 0);

        //IF it is not the first time to use this app, read the shared preferences data.
        if(size != 0) {
            for(int i=0; i<size; i++){
                firstLists.add(mPreferences.getBoolean(HiConfig.SWITCH_STATUS, false));
            }
            HiLog.d("the size of first list is ","" + firstLists.size());
        } else {
            //IF it is the first time to use this app, init and save data to shared preferences.
            SharedPreferences.Editor editor = mSelf.getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
            firstLists.add(false);
            firstLists.add(true);
            firstLists.add(false);

            editor.putInt(HiConfig.STATUS_NUM,firstLists.size());
            for(int i=0; i< firstLists.size(); i++){
                editor.putBoolean(HiConfig.SWITCH_STATUS + i, firstLists.get(i));
            }

            editor.commit();
            HiLog.d("the initial size of first list is ","" + firstLists.size());
        }

        return  firstLists;

    }


    //TODO:kaikai added
    /**
     * init switch status
     * @return

    public List<Boolean> getSwitchStatusList(){
         List<Boolean> statusList = new ArrayList<>();
        statusList.add(false);
        statusList.add(false);
        statusList.add(false);
        return statusList;
    }*/


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSelItemPosition = position;
        Intent intent = new Intent(mSelf, HiTimePickerActivity.class);
        intent.putExtra("category", mAlarmList.get(position).getCategory());
        intent.putExtra("ItemPosition", position);
        //TODO 选择时间，返回时间值，设定相应条目

        startActivityForResult(intent, FLAG_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //接收设定时间后的时间
        if (requestCode == FLAG_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            int hour = data.getIntExtra("hour", 00);
            int minute = data.getIntExtra("minute", 00);
            String selTime = hour + " : " + minute;
            mAlarmList.get(mSelItemPosition).setTime(selTime);
            mTimeListAdapter.notifyDataSetChanged();
        }

    }

    class HiTimeListAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater inflater;
        private List<HiAlarmTab> mDataList;

        private class HiHandler {
            private ImageView imgIcon;
            private TextView tvCategory;
            private TextView tvTime;
            private Switch Switch;
        }

        public HiTimeListAdapter(Context c, List<HiAlarmTab> dataList) {
            this.mContext = c;
            this.inflater = LayoutInflater.from(c);
            this.mDataList = dataList;
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            HiHandler handler = null;
            SharedPreferences.Editor editor = mSelf.getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
            if (convertView == null) {
                handler = new HiHandler();
//                convertView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.item_sleep_time, null);
                convertView = inflater.inflate(R.layout.item_sleep_time, null);
//                convertView = mSelf.getLayoutInflater().inflate(R.layout.item_sleep_time, null);
                handler.imgIcon = (ImageView) convertView.findViewById(R.id.img_icon);
                handler.tvCategory = (TextView) convertView.findViewById(R.id.text_ready_time);
                handler.tvTime = (TextView) convertView.findViewById(R.id.text_ready_time_point);
                handler.Switch = (Switch) convertView.findViewById(R.id.switch_ready_time);
                convertView.setTag(handler);
            } else {
                handler = (HiHandler) convertView.getTag();
            }
            handler.imgIcon.setImageResource((Integer) mDataList.get(position).getIcon());
            handler.tvCategory.setText((String) mDataList.get(position).getCategory());
            handler.tvTime.setText((String) mDataList.get(position).getTime());

            //init the new switch flag
            //TODO: it it tricky to init without any judgement.
            mSwitchStatusList.add(false);
            editor.putInt(HiConfig.STATUS_NUM, mSwitchStatusList.size());
            editor.putBoolean(HiConfig.SWITCH_STATUS + mSwitchStatusList.size(), false);
            editor.commit();

            handler.Switch.setChecked( mSwitchStatusList.get(position));

            //TODO:alarm clock switch setting.
            handler.Switch.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            HiLog.d("switch checked","" + isChecked);

                            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(mSelf.ALARM_SERVICE);
                            Intent alarmIntent = new Intent(getActivity(), HiAlarmClockReceiver.class);
                            String category = (String) mDataList.get(position).getCategory();

                            if (isChecked) {
                                String h = mDataList.get(position).getTime().split(":")[0].trim();
                                String m = mDataList.get(position).getTime().split(":")[1].trim();
                                int hour = Integer.parseInt(h);
                                int minute = Integer.parseInt(m);

                                HiLog.i(TAG,"hour->" + hour + "; minute->" + minute);
                                Calendar calendar =  Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);

                                //设置的时间早于当前时间，则说明应该在第二天响铃
                                if(Calendar.getInstance().getTimeInMillis() > calendar.getTimeInMillis()) {
                                    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
                                }

                                alarmIntent.putExtra("id", position);
                                alarmIntent.putExtra("switch", isChecked);
                                alarmIntent.putExtra("category", category);

                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), position, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                                HiLog.d(TAG, "position " + position);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);

//                                ELAPSED_REALTIME：         从设备启动之后开始算起，度过了某一段特定时间后，激活Pending Intent，但不会唤醒设备。其中设备睡眠的时间也会包含在内。
//                                ELAPSED_REALTIME_WAKEUP：  从设备启动之后开始算起，度过了某一段特定时间后唤醒设备。
//                                RTC：                      在某一个特定时刻激活Pending Intent，但不会唤醒设备。
//                                RTC_WAKEUP：               在某一个特定时刻唤醒设备并激活Pending Intent。
                            } else {
                                alarmIntent.putExtra("id", position);
                                alarmIntent.putExtra("switch",isChecked);
                                alarmIntent.putExtra("category", category);

                                //Intent i=new Intent(getActivity(),HiAlarmClockReceiver.class);
                                //PendingIntent pi = PendingIntent.getBroadcast(getActivity(), position , alarmIntent, 0);
                                //alarmManager.cancel(pi);//取消闹钟
                                getContext().sendBroadcast(alarmIntent);
                            }

                        }
                    }
            );

            return convertView;
        }


    }


}
