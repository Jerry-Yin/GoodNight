package com.hdu.team.hiwanan.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.activity.HiTimePickerActivity;
import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.view.HiTimeTabManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiHomePageFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

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
    private ImageButton mbtnAddClock;
    private TextView mTvSleepTime;
    private ListView mListView;

    /**
     * Values
     */
    private BaseAdapter mTimeListAdapter;
    private List<Map<String, Object>> mTimeList;

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
            mContentView = inflater.inflate(R.layout.layout_home_page, container, false);
            mSelf = getActivity();
            setupViews();
            initTime();
        }
        return mContentView;
    }

    private void setupViews() {
        mbtnAddClock = (ImageButton) mContentView.findViewById(R.id.btn_add_clock);
        mbtnAddClock.setOnClickListener(this);

//        mTvSleepTime = (TextView) mContentView.findViewById(R.id.text_ready_time_point);
//        mTvSleepTime.setOnClickListener(this);

        mListView = (ListView) mContentView.findViewById(R.id.list_sleep_time);
        mTimeList = getFirstLists();
        mTimeListAdapter = new HiTimeListAdapter(mSelf, mTimeList);
        mListView.setAdapter(mTimeListAdapter);
//        mTimeListAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(this);
    }

    /**
     * todo
     * 界面初始化，进入界面从sp数据库中判断是否有已经设定好的时间tab数，如果有，就拿出来设置；没有就默认；
     * 判断时需要判断多个tab item 的数据，并一次设定界面
     * 遍历mTimeList，
     */
    private void initTime() {
        mPreferences = mSelf.getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (mTimeList.size() != 0) {
            for (int i = 0; i < mTimeList.size(); i++) {
                long hour = mPreferences.getLong(HiConfig.DIF_HOURS + i, 00);
                long minute = mPreferences.getLong(HiConfig.DIF_MINUTES + i, 00);
                String time = hour + " : " + minute;
                mTimeList.get(i).put("time",time);
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
                        Map<String, Object> map = HiTimeTabManager.createMapTab(R.drawable.ic_access_alarm_black_24dp, (String) clock_category[which], "00 : 00", mSelf);
                        mTimeList.add(map);
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
    public List<Map<String, Object>> getFirstLists() {
        List<Map<String, Object>> firstLists = new ArrayList<>();
        //TODO 添加三条初始化的tab
        Map<String, Object> map1 = HiTimeTabManager.createMapTab(R.drawable.ic_access_alarm_black_24dp, HiTimeTabManager.CATEGORY_READY, "10 : 00", mSelf);
        Map<String, Object> map2 = HiTimeTabManager.createMapTab(R.drawable.ic_access_alarm_black_24dp, HiTimeTabManager.CATEGORY_SLEEP, "10 : 30", mSelf);
        Map<String, Object> map3 = HiTimeTabManager.createMapTab(R.drawable.ic_access_alarm_black_24dp, HiTimeTabManager.CATEGORY_GETUP, "08 : 00", mSelf);
        firstLists.add(map1);
        firstLists.add(map2);
        firstLists.add(map3);
        return firstLists;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSelItemPosition = position;
        Intent intent = new Intent(mSelf, HiTimePickerActivity.class);
        intent.putExtra("category", mTimeList.get(position).get("category").toString());
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
            mTimeList.get(mSelItemPosition).put("time", selTime);
            mTimeListAdapter.notifyDataSetChanged();
        }

    }

    class HiTimeListAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater inflater;
        private List<Map<String, Object>> mDataList;

        private class HiHandler {
            private ImageView imgIcon;
            private TextView tvCategory;
            private TextView tvTime;
            private Switch Switch;
        }

        public HiTimeListAdapter(Context c, List<Map<String, Object>> dataList) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            HiHandler handler = null;
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
            handler.imgIcon.setImageResource((Integer) mDataList.get(position).get("icon"));
            handler.tvCategory.setText((String) mDataList.get(position).get("category"));
            handler.tvTime.setText((String) mDataList.get(position).get("time"));
            return convertView;
        }


    }


}
