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

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.hdu.team.hiwanan.constant.HiResponseCodes;
import com.hdu.team.hiwanan.database.HiGoodNightDB;
import com.hdu.team.hiwanan.listener.OnResponseListener;
import com.hdu.team.hiwanan.manager.HiAlarmTaskPoolManager;
import com.hdu.team.hiwanan.model.HiAlarmTab;
import com.hdu.team.hiwanan.model.HiAlarmTask;
import com.hdu.team.hiwanan.model.HiRespSleepTime;
import com.hdu.team.hiwanan.service.HiScreenLockService;
import com.hdu.team.hiwanan.util.HiLog;
import com.hdu.team.hiwanan.util.HiToast;
import com.hdu.team.hiwanan.util.OkHttpUtils;
import com.hdu.team.hiwanan.util.common.GsonUtils;
import com.hdu.team.hiwanan.util.common.HiTimesUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiClockFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "HiClockFragment";
    private HiGoodNightDB mGoodNightDB;

    private static final int FLAG_REQUEST_CODE = 001;
    //private static final String CREATE_CLOCK = "请选择闹钟时间类型";
    //private CharSequence[] clock_category = new CharSequence[]{"预备时间", "入睡时间", "早起时间"};
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

    private LineChartView mLineChartView;
    private ColumnChartView mColumnChartView;
    private Switch mSwitchWeek;


    /**
     * Values
     */
    private BaseAdapter mTimeListAdapter;
    private List<HiAlarmTab> mAlarmList;
    //TODO:kaikai added list value for switch status.
    private List<Boolean> mSwitchStatusList;

    private int mSelItemPosition;   //当前选中的item位置

    private SharedPreferences mPreferences;
    private HiAlarmTaskPoolManager mAlarmTaskPoolManager;

    public Activity getmSelf() {
        return mSelf;
    }


    private LineChartData mLineChartData;       //折线图数据
    private List<AxisValue> mAxisValuesX = new ArrayList<>();    //X坐标值
    private List<AxisValue> mAxisValuesY = new ArrayList<>();    //Y坐标值
    private boolean mIsWeek = true;  //默认坐标是显示周的数据
    private List<Line> mLines = new ArrayList<Line>();   //
    private Line mLineStandard; //设定睡眠时间 线
    private Line mLineReal;     //实际睡眠时间 线
    private List<PointValue> mPointValuesPlan = new ArrayList<PointValue>();  //设定睡眠时间数据 点
    private List<PointValue> mPointValuesReal = new ArrayList<PointValue>();  //实际睡眠时间数据 点

    private List<String> mDateList = null;  //横坐标日期


    private ColumnChartData mColumnChartData;   //柱状图数据


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
            mSelf = this.getActivity();
            mGoodNightDB = HiGoodNightDB.getInstance(mSelf);
            mAlarmTaskPoolManager = HiAlarmTaskPoolManager.getInstance();
            // first time using check.
            firstLoad();
            setupViews();
            initData();
        }
        return mContentView;
    }


    /**
     * @return
     * @author Kaikai, Fu
     * to check if the user is the first time to open this app.
     */
    private boolean isFirstLoad() {
        mPreferences = mSelf.getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, Context.MODE_PRIVATE);
        boolean firstLoad = mPreferences.getBoolean(HiConfig.FIRST_LOAD, true);

        if (firstLoad) {
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(HiConfig.FIRST_LOAD, false);
            editor.commit();
        }

        return firstLoad;
    }

    private void firstLoad() {
        if (isFirstLoad()) {
            //if it is the first time user open this app, create 3 default alarm tab items.
            //int id, int icon, String category, String time, boolean on, int musicId)
            HiAlarmTab tab1 = new HiAlarmTab(0, R.drawable.ic_access_alarm_black_24dp, HiAlarmTab.CATEGORY_READY, "10:00", false, R.raw.voice);
            HiAlarmTab tab2 = new HiAlarmTab(1, R.drawable.ic_access_alarm_black_24dp, HiAlarmTab.CATEGORY_SLEEP, "10:30", false, R.raw.voice);
            HiAlarmTab tab3 = new HiAlarmTab(2, R.drawable.ic_access_alarm_black_24dp, HiAlarmTab.CATEGORY_GETUP, "08:00", false, R.raw.voice);

            mGoodNightDB.saveAlarmTab(tab1);
            mGoodNightDB.saveAlarmTab(tab2);
            mGoodNightDB.saveAlarmTab(tab3);

            mAlarmTaskPoolManager.addTask(new HiAlarmTask(mSelf, tab1.getId(), tab1.getMusicId()));
            mAlarmTaskPoolManager.addTask(new HiAlarmTask(mSelf, tab2.getId(), tab1.getMusicId()));
            mAlarmTaskPoolManager.addTask(new HiAlarmTask(mSelf, tab3.getId(), tab1.getMusicId()));
        }
    }

    private void setupViews() {
        mbtnAddClock = (FloatingActionButton) mContentView.findViewById(R.id.btn_add_clock);
        mbtnAddClock.setOnClickListener(this);
        mListView = (ListView) mContentView.findViewById(R.id.list_sleep_time);
        mLineChartView = (LineChartView) mContentView.findViewById(R.id.line_chart_view);
        mColumnChartView = (ColumnChartView) mContentView.findViewById(R.id.column_chart_view);
        mSwitchWeek = (Switch) mContentView.findViewById(R.id.switch_change);

        //load the data from db.
        mAlarmList = mGoodNightDB.queryAllAlarmTabs();

        mTimeListAdapter = new HiTimeListAdapter(mSelf, mAlarmList);
        mListView.setAdapter(mTimeListAdapter);
        mTimeListAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mSwitchWeek.setOnCheckedChangeListener(this);
    }


    private void initData() {
        mLineChartData = new LineChartData();
        initAxisY();
        switchAxisX(mIsWeek);
        fetchSleepTimeData(mIsWeek);
//        setPoints();
//        initLines();
//        refreshLineView();
    }

    private void switchAxisX(boolean isWeek) {
        mAxisValuesX.clear();
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);     //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setHasLines(true);            //x 轴分割线
        axisX.setAutoGenerated(true);
        axisX.setInside(false);            //
//        axisX.setName("日期/d");
        axisX.setTextSize(10);
        axisX.setTextColor(getResources().getColor(R.color.white));
//        axisX.setMaxLabelChars(8);
        if (isWeek) {
            //week 7days
            mDateList = HiTimesUtil.getLastWeekDate();
            for (int i = 0; i < mDateList.size(); i++) {
                AxisValue value = new AxisValue(i);
                String label = mDateList.get(i);
//                HiLog.d(TAG, "week" +i + ":" + label);
                value.setLabel(label);
                mAxisValuesX.add(value);
            }
        } else {
            //month 6x5=30days
            mDateList = HiTimesUtil.getLastMonthDate();
//            int x = 0;
            for (int i = 0; i < mDateList.size(); i++) {
//                if (i % 5 == 0) {
                AxisValue value = new AxisValue(i);
                String label = mDateList.get(i);
                value.setLabel(label);
//                    HiLog.d(TAG, "month" +i + ":" + label);
                mAxisValuesX.add(value);
//                    x++;
//                }
            }
        }
        axisX.setValues(mAxisValuesX);
        mLineChartData.setAxisXBottom(axisX);
        refreshLineView();
    }

    private void refreshLineView() {
//        mLineChartView.setViewportCalculationEnabled(false);
//        mLineChartData.finish();
        mLineChartData.setBaseValue(Float.NEGATIVE_INFINITY);
        mLineChartView.setLineChartData(mLineChartData);

        Viewport viewport = mLineChartView.getMaximumViewport();
        viewport.set(viewport.left, (float) (viewport.top*1.5), viewport.right, 0);
        HiLog.d(TAG, "left1: "+viewport.left);
        HiLog.d(TAG, "top1: "+viewport.top);    //top = maxValue   ( top*1.2): to set the maxY = 1.2*MaxValue
        HiLog.d(TAG, "right1: "+viewport.right);
        HiLog.d(TAG, "bottom1: "+viewport.bottom);
        mLineChartView.setMaximumViewport(viewport);

//        viewport = mLineChartView.getCurrentViewport();
//        viewport.set(viewport.left, viewport.top, viewport.right, 0);
//        HiLog.d(TAG, "left2: "+viewport.left);
//        HiLog.d(TAG, "top2: "+viewport.top);
//        HiLog.d(TAG, "right2: "+viewport.right);
//        HiLog.d(TAG, "bottom2: "+viewport.bottom);
        mLineChartView.setCurrentViewport(viewport);

    }

    //设置折线图坐标
    private void initAxisY() {
//        Axis axisX = new Axis(); //X轴
//        axisX.setHasTiltedLabels(false);     //X坐标轴字体是斜的显示还是直的，true是斜的显示
//        axisX.setHasLines(true);            //x 轴分割线
//        axisX.setAutoGenerated(true);
//        axisX.setInside(false);            //
////        axisX.setName("日期/d");
//        axisX.setTextSize(10);
//        axisX.setTextColor(getResources().getColor(R.color.white));
////        axisX.setMaxLabelChars(8);
//        for (int i = 0; i <= 7; i++) {
//            AxisValue value = new AxisValue(i);
//            String label = HiTimesUtil.getCurWeek(i);
//            value.setLabel(label);
//            mAxisValuesX.add(value);
//        }
//        axisX.setValues(mAxisValuesX);
//        mLineChartData.setAxisXBottom(axisX);

//        Axis axisY = Axis.generateAxisFromRange(0, 24, 4);  //Y轴
        if (mAxisValuesY != null) {
            mAxisValuesY.clear();
        }
        Axis axisY = new Axis();  //Y轴
        axisY.setHasTiltedLabels(false);
        axisY.setHasLines(true);
        axisY.setAutoGenerated(true);
        axisY.setInside(false);
//        axisY.setName("睡眠时长/h");
//        axisY.setMaxLabelChars(24); //默认是3，只能看最后三个数字
        axisY.setTextSize(10);
        axisY.setTextColor(getResources().getColor(R.color.white));
        for (int i = 0; i <= 24; i++) {
            AxisValue value = new AxisValue(i);
            value.setLabel(String.valueOf(i));
            mAxisValuesY.add(value);
//            i+=4;
        }
        axisY.setValues(mAxisValuesY);
        mLineChartData.setAxisYLeft(axisY);
    }


    private void fetchSleepTimeData(boolean isWeek) {
        int userId = 2;
        int duration;
        if (isWeek) {
            duration = 7;
        } else {
            duration = 30;
        }
//        List<HiMaps> params = new ArrayList<>();
//        params.add(new HiMaps("action", "query"));
//        String p1 = "\"action\":\"query\"";
//        String p = "{\"userId\":2, \"duration\":7}";
////        String json = "{"+"userId"+":" + userId + "," + "+duration:" + duration + "}";
////        JsonArray array = new JsonArray();
////        JsonObject o1 = new JsonObject();
////        o1.addProperty("userId", userId);
////        JsonObject o2 = new JsonObject();
////        o2.addProperty("duration", duration);
////        array.add(o1);
////        array.add(o2);
//        params.add(new HiMaps("params", p));
//        HiLog.d(TAG, "params: "+params);

        String params = "{\"action\":\"query\",\"params\":{\"userId\":" + userId + ",\"duration\":" + duration + "}}";

        OkHttpUtils.sendPost(HiConfig.URL_GET_SLEEP, params, new OnResponseListener<String>() {
            Message message = new Message();

            @Override
            public void onSuccess(String result) {
                HiLog.d(TAG, result.toString());
                HiRespSleepTime response = GsonUtils.parseJsonToClass((String) result, HiRespSleepTime.class);
                if (response != null) {
                    if (response.getStatus() == 1) {
                        message.what = HiResponseCodes.SLEEP_TIME_OK;
                    } else {
                        message.what = HiResponseCodes.SLEEP_TIME_FAIL;
                    }
                    message.obj = response.getResult();
                    Looper.prepare();
                    mHandler.sendMessage(message);
                    Looper.loop();
                }
            }

            @Override
            public void onFailure(int errorCode, String error) {
                HiLog.d(TAG, errorCode + error);
                message.what = HiResponseCodes.SLEEP_TIME_FAIL;
                message.obj = errorCode + error;
                Looper.prepare();
                mHandler.handleMessage(message);
                Looper.loop();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        //initTime();
        //TODO: refresh view if data changed, can add a standalone method to handle this task.
        setupViews();
        mTimeListAdapter.notifyDataSetChanged();
        mSelf.startService(new Intent(mSelf, HiScreenLockService.class));

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HiResponseCodes.SLEEP_TIME_OK:
                    List<HiRespSleepTime.SleepTimeLine> timeLine = (List<HiRespSleepTime.SleepTimeLine>) msg.obj;
                    HiLog.d(TAG, "timeLine= " + timeLine);
                    refreshTimeLineData(mIsWeek, timeLine);
                    break;
                case HiResponseCodes.SLEEP_TIME_FAIL:
                    String result = msg.obj.toString();
                    HiToast.showToast(mSelf, result);
                    break;
            }
        }
    };

    /**
     * 根据数据刷新图标界面
     *
     * @param mIsWeek
     * @param timeLine
     */
    private void refreshTimeLineData(boolean mIsWeek, List<HiRespSleepTime.SleepTimeLine> timeLine) {
        setPoints(mIsWeek, timeLine);
        initLines();
        refreshLineView();
    }

    //初始化 点 数据
    private void setPoints(boolean mIsWeek, List<HiRespSleepTime.SleepTimeLine> timeLine) {
        if (mPointValuesReal != null && mPointValuesReal.size() > 0) {
            mPointValuesReal.clear();
        }
        if (mPointValuesPlan != null && mPointValuesPlan.size() > 0) {
            mPointValuesPlan.clear();
        }
//        int length = 0;
////        int x = 0;
//        if (mIsWeek) {
//            length = 7;
//        } else {
//            length = 30;
//        }
        for (int i = 0; i < mDateList.size(); i++) {
            String date = mDateList.get(i);
            HiRespSleepTime.SleepTimeLine line = getLineByDate(timeLine, date);
            if (line == null){
                mPointValuesPlan.add(new PointValue(i, 0));
                mPointValuesReal.add(new PointValue(i, 0));
            }else {
                mPointValuesPlan.add(new PointValue(i, line.getPlan_duration()));
                mPointValuesReal.add(new PointValue(i, line.getActual_duration()));
            }
        }
    }

    /**
     * is timeLine contains date
     * @param timeLine  2017-05-02
     * @param date  05.02
     * @return
     */
    private HiRespSleepTime.SleepTimeLine getLineByDate(List<HiRespSleepTime.SleepTimeLine> timeLine, String date) {
        for (HiRespSleepTime.SleepTimeLine line : timeLine){
            String[] d = line.getDate().split("-");
            if ((d[1]+"."+d[2]).equals(date)){
                return line;
             }
        }
        return null;
    }

    //设置线条数据

    /**
     * Line line = new Line(highPointValues).setColor(Color.parseColor("#C0D79C")).setStrokeWidth(1);  //折线的颜色、粗细
     * line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
     * line.setCubic(true);//曲线是否平滑
     * line.setFilled(false);//是否填充曲线的面积
     * 　 line.setHasLabels(true);//曲线的数据坐标是否加上备注
     * line.setPointRadius(3);　//座标点大小
     * line.setHasLabelsOnlyForSelected(false);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
     * line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
     * line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
     * lines.add(line);
     * LineChartData data = new LineChartData();
     * data.setValueLabelBackgroundColor(Color.TRANSPARENT);　　　　//此处设置坐标点旁边的文字背景
     * data.setValueLabelBackgroundEnabled(false);
     * data.setValueLabelsTextColor(Color.BLACK);  //此处设置坐标点旁边的文字颜色
     */
    private void initLines() {
        if (mLineStandard != null)
            mLineStandard = null;
        if (mLineReal != null)
            mLineReal = null;
        mLineStandard = new Line(mPointValuesPlan)
                .setColor(getResources().getColor(R.color.btn_out_color))
                .setCubic(false)
                .setStrokeWidth(1)
                .setPointRadius(2);
        mLineReal = new Line(mPointValuesReal)
                .setColor(getResources().getColor(R.color.white))
                .setCubic(false)
                .setStrokeWidth(1)
                .setPointRadius(2)
                .setFilled(true);
//        mLineStandard.setShape(ValueShape.DIAMOND);
//        mLineReal.setShape(ValueShape.SQUARE);
        mLines.clear();
        mLines.add(mLineStandard);
        mLines.add(mLineReal);
        mLineChartData.setLines(mLines);
    }

    /**
     * switch change the view to week or month
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mIsWeek = isChecked;
        switchAxisX(isChecked);
        fetchSleepTimeData(isChecked);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_clock:
                // todo 添加闹钟（类型，时间）
//                AlertDialog.Builder builder = new AlertDialog.Builder(mSelf);
//                builder.setTitle(CREATE_CLOCK)
//                .setItems(clock_category, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        HiAlarmTab map = HiAlarmTabManager.createMapTab(mAlarmList.size(), R.drawable.ic_access_alarm_black_24dp, (String) clock_category[which], "00 : 00", false, 0);
//                        mAlarmList.add(map);
//                        mTimeListAdapter.notifyDataSetChanged();
//                    }
//                })
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mTimeListAdapter.notifyDataSetChanged();
//                    }
//                })
//                .create().show();

                //TODO:kaikai transfer alarm clock creating to HiTimePickerActivity.
                Intent intent = new Intent(mSelf, HiTimePickerActivity.class);
                //flag for identify the request is create or modify the alarm clock;
                intent.putExtra(HiConfig.REQUEST_TYPE, HiConfig.CREATE_REQUEST);
                startActivity(intent);

                //here didn't finish the activity for still living after create or modified the alarm clock.
                //mSelf.finish();
                break;

            case R.id.text_ready_time_point:

                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mSelItemPosition = position;
//        Intent intent = new Intent(mSelf, HiTimePickerActivity.class);
//        intent.putExtra("category", mAlarmList.get(position).getCategory());
//        intent.putExtra("ItemPosition", position);
//        //TODO 选择时间，返回时间值，设定相应条目
//
//        startActivityForResult(intent, FLAG_REQUEST_CODE);
        Intent intent = new Intent(mSelf, HiTimePickerActivity.class);
        intent.putExtra(HiConfig.REQUEST_TYPE, HiConfig.MODIFY_REQUEST);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mSelf);
        builder.setMessage("移除此闹钟")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeAlarmTab(position);
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();

        return false;
    }

    /**
     * delete tab from view & db & taskPool
     *
     * @param position
     */
    private void removeAlarmTab(int position) {
        mGoodNightDB.deleteAlarmTab(position);

        mAlarmTaskPoolManager.removeTask(mAlarmTaskPoolManager.getTaskById(position));

        mAlarmList.remove(position);
        mTimeListAdapter.notifyDataSetChanged();

        if (mAlarmList.size() == 0)
            mGoodNightDB.clearAlarmTab();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //接收设定时间后的时间
//        /**if (requestCode == FLAG_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            int hour = data.getIntExtra("hour", 00);
//            int minute = data.getIntExtra("minute", 00);
//            String selTime = hour + " : " + minute;
//            mAlarmList.get(mSelItemPosition).setTime(selTime);
//            mTimeListAdapter.notifyDataSetChanged();
//        }**/
//
//    }

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
            //SharedPreferences.Editor editor = mSelf.getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
            if (convertView == null) {
                handler = new HiHandler();
//                convertView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.layout_item_sleep_time, null);
                convertView = inflater.inflate(R.layout.layout_item_sleep_time, null);
//                convertView = mSelf.getLayoutInflater().inflate(R.layout.layout_item_sleep_time, null);
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
            handler.Switch.setChecked(mDataList.get(position).isOn());
            //TODO:alarm clock switch setting.
            handler.Switch.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            HiLog.d("switch checked", "" + isChecked);

                            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(mSelf.ALARM_SERVICE);
                            Intent alarmIntent = new Intent(getActivity(), HiAlarmClockReceiver.class);
                            String category = (String) mDataList.get(position).getCategory();

                            alarmIntent.putExtra("id", position);
                            alarmIntent.putExtra("switch", isChecked);
                            alarmIntent.putExtra("category", category);

                            if (isChecked) {
                                String h = mDataList.get(position).getTime().split(":")[0].trim();
                                String m = mDataList.get(position).getTime().split(":")[1].trim();
                                int hour = Integer.parseInt(h);
                                int minute = Integer.parseInt(m);

                                HiLog.i(TAG, "hour->" + hour + "; minute->" + minute);
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);

                                //设置的时间早于当前时间，则说明应该在第二天响铃
                                if (Calendar.getInstance().getTimeInMillis() > calendar.getTimeInMillis()) {
                                    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
                                }

                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), position, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                                HiLog.d(TAG, "position " + position);
                                //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
                                //TODO:重复闹钟设定,每天重复
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

//                                ELAPSED_REALTIME：         从设备启动之后开始算起，度过了某一段特定时间后，激活Pending Intent，但不会唤醒设备。其中设备睡眠的时间也会包含在内。
//                                ELAPSED_REALTIME_WAKEUP：  从设备启动之后开始算起，度过了某一段特定时间后唤醒设备。
//                                RTC：                      在某一个特定时刻激活Pending Intent，但不会唤醒设备。
//                                RTC_WAKEUP：               在某一个特定时刻唤醒设备并激活Pending Intent。
                            } else {
                                //Intent i=new Intent(getActivity(),HiAlarmClockReceiver.class);
                                //PendingIntent pi = PendingIntent.getBroadcast(getActivity(), position , alarmIntent, 0);
                                //alarmManager.cancel(pi);//取消闹钟
                                getContext().sendBroadcast(alarmIntent);
                            }
                            updateSwitchDb(position, isChecked);
//                            if (!HiNotificationUtil.isNotificationListenEnabled(mSelf)) {
//                                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//                                startActivity(intent);
//                            } else {
//                                HiToast.showToast(mSelf, "已成功开启通知服务权限");
//                            }
                        }
                    }
            );

            return convertView;
        }


    }

    /**
     * update switch status to db when status changed
     *
     * @param id
     * @param on
     */
    private void updateSwitchDb(int id, boolean on) {
        HiAlarmTab tab = mAlarmList.get(id);
        tab.setOn(on);
        mTimeListAdapter.notifyDataSetChanged();
        mGoodNightDB.updateAlarmTab(tab);
    }


}
