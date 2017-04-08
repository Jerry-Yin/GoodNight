package com.hdu.team.hiwanan.fragments;


import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.adapter.CusRecyclerViewAdapter;
import com.hdu.team.hiwanan.constant.HiResponseCodes;
import com.hdu.team.hiwanan.model.HiCalendar;

import android.widget.ScrollView;
import android.widget.TextView;


import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.listener.OnResponseListener;
import com.hdu.team.hiwanan.model.HiCalendarData;
import com.hdu.team.hiwanan.network.BmobNetworkUtils;
import com.hdu.team.hiwanan.util.HiLog;
import com.hdu.team.hiwanan.util.OkHttpUtils;
import com.hdu.team.hiwanan.util.common.GsonUtils;
import com.hdu.team.hiwanan.util.common.HiTimesUtil;


/**
 * Created by JerryYin on 3/9/17.
 */

public class HiCalendarFragment2 extends Fragment {

    /**
     * Constants
     */
    private View mContentView;
    private static final java.lang.String TAG = "HiCalendarFragment2";
    private Activity mSelf;

    /**Views*/
//    private RecyclerView mRecyclerView;
//    private ImageView mImageView;
    private ScrollView mScrollView;
    private TabLayout mTabLayout;
    private View mTitleView;

    private TextView year;
    private TextView month;           //月份
    private TextView day1;            //新历日期
    private TextView weekend;         //周几
    private TextView year_old;        //农历年份
    private TextView year_zodiac;     //生肖
    private TextView month_zodiac;    //农历月份
    private TextView day_zodiac;      //日
    private TextView day;
    private TextView suit;    //宜做事
    private TextView avoio;   //不适合做事
    private TextView words;           //名言
    private TextView author;          //作者




    /**Values*/
    private HiCalendar mCalendar;
    private CusRecyclerViewAdapter mViewAdapter;

    private float mDistanceY;   //滑动高度
    private int mTabHeight;     //标题栏高度
    public int mWhichTab = 0;   //标识位，确定该显示哪个标题

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != mContentView) {
            ViewGroup vg = (ViewGroup) mContentView.getParent();
            if (null != vg) {
                vg.removeView(mContentView);
            }
        } else {
            mSelf = getActivity();
            mContentView = inflater.inflate(R.layout.layout_calendar2, null);
            initViews();
            initData();
            HiLog.d(TAG, "main : "+Thread.currentThread().getId());
        }
        return mContentView;
    }

    public void initViews() {
        mScrollView = (ScrollView) mContentView.findViewById(R.id.sv_calendar);
        mTitleView = mSelf.findViewById(R.id.title_view_share);
        year = (TextView) mContentView.findViewById(R.id.text_year);
        month = (TextView) mContentView.findViewById(R.id.text_month);
        day1 = (TextView) mContentView.findViewById(R.id.text_day1);
        weekend = (TextView) mContentView.findViewById(R.id.text_weekend);
        year_old = (TextView) mContentView.findViewById(R.id.text_year_old);
        year_zodiac = (TextView) mContentView.findViewById(R.id.text_year_zodiac);
        month_zodiac = (TextView) mContentView.findViewById(R.id.text_month_zodiac);
        day = (TextView) mContentView.findViewById(R.id.text_day);
        suit = (TextView) mContentView.findViewById(R.id.text_suit);
        avoio = (TextView) mContentView.findViewById(R.id.text_avoio);
        words = (TextView) mContentView.findViewById(R.id.text_words);
        author = (TextView) mContentView.findViewById(R.id.text_author);

    }


    public void initData() {
        // TODO: 10/13/16 add data
        mTabHeight = mScrollView.getBottom();
        initCalendars();
        initScroll();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initScroll() {
        mTabLayout = (TabLayout) mSelf.findViewById(R.id.tab_layout);
        day_zodiac = (TextView) mContentView.findViewById(R.id.text_day_zodiac);
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                HiLog.d(TAG, "scrollY: "+scrollY);
                HiLog.d(TAG, "oldY: "+oldScrollY);
//                mDistanceY += scrollY > oldScrollY ? scrollY-oldScrollY : 0;
                mDistanceY = scrollY;
                int titleHeight = mTabLayout.getBottom();
                HiLog.d(TAG, "height: "+titleHeight);
                HiLog.d(TAG, "distance: "+mDistanceY);

                /**
                 * 绿色：#00BFA5
                 * 三原色：(0 - 191 - 165) (可通过PS获取)
                 * 高度没到达时候。渐变
                 * 白色：#ffffff (255 - 255 - 255)
                 */
                if (mDistanceY == 0){
                    mTitleView.setVisibility(View.GONE);
                    mTabLayout.setVisibility(View.VISIBLE);
                    mWhichTab = 0;
                }
                if(mDistanceY <= titleHeight){
                    HiLog.d(TAG, "distance: "+mDistanceY);
                    float scale = (float) mDistanceY/titleHeight;
                    float alpha = scale * 255;
                    HiLog.d(TAG, "scale: "+scale);
                    HiLog.d(TAG, "alpha: "+alpha);
                    HiLog.d(TAG, "---------------------------");
                    mTabLayout.setBackgroundColor(Color.argb((int) alpha, 0, 191, 165));
                    mTitleView.setBackgroundColor(Color.argb((int) alpha, 0, 191, 165));
                }else {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.title_color));
                    mTabLayout.setVisibility(View.GONE);
                    mTitleView.setVisibility(View.VISIBLE);
                    mWhichTab = 1;
                }

            }
        });

    }



    /**
     * "{\"reason\":\"Success\",\"result\":{\"data\":{\"avoid\":\"开光.嫁娶.\",\"animalsYear\":\"鸡\",\"weekday\":\"星期六\",\"suit\":\"破屋.坏垣.求医.治病.余事勿取.\",\"lunarYear\":\"丁酉年\",\"lunar\":\"二月十四\",\"year-month\":\"2017-3\",\"date\":\"2017-3-11\"}},\"error_code\":0}"
     */
    private void initCalendars() {
        String date = HiTimesUtil.getCurDateNoZero();
        HiLog.d(TAG, date);


//      请求示例：http://japi.juhe.cn/calendar/day?date=2015-1-1&key=您申请的appKey
        String url = HiConfig.URL_CALENDAR + "?date=" + date +"&key="+HiConfig.APP_KEY_CALENDAR;
        OkHttpUtils.OkHttpGet(url, new OnResponseListener() {
            @Override
            public void onSuccess(Object result) {
                String data  = GsonUtils.parseJsonToString(result);
                HiLog.d(TAG, "result : "+result.toString());
                HiLog.d(TAG, "success : "+data);
//                HiCalendarData calendarData = GsonUtils.parseJsonToClass(data, HiCalendarData.class);
                Gson gson = new Gson();
//                String d = gson.toJson(result);

                HiCalendarData calendarData = gson.fromJson(String.valueOf(result), HiCalendarData.class);
                Message message  = new Message();
                message.what = HiResponseCodes.CALENDAR_OK;
                message.obj = calendarData;
                HiLog.d(TAG, "network : "+Thread.currentThread().getId());
                mHandler.sendMessage(message);

            }

            @Override
            public void onFailure(int errorCode, String error) {
                Message message  = new Message();
                message.what = HiResponseCodes.CALENDAR_FAIL;
                message.obj = errorCode + error;
                mHandler.sendMessage(message);
                HiLog.d(TAG, "error "+ errorCode + " "+ error);
            }
        });

        initCalendarSums();

    }

    /**
     * 初始化日历的（点赞数 + 评论数 + 分享数）
     */
    private void initCalendarSums() {
        // TODO: 4/8/17 init by today date
        String today = HiTimesUtil.getCurDate();
        BmobNetworkUtils.queryCalendar(today, new OnResponseListener() {
            @Override
            public void onSuccess(Object result) {
                HiLog.d(TAG, result.toString());
            }

            @Override
            public void onFailure(int errorCode, String error) {
                HiLog.d(TAG, errorCode + error);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        showWhichTab();
    }

    private void showWhichTab() {
        if(mDistanceY < mTabHeight){
            mTabLayout.setVisibility(View.VISIBLE);
            mTitleView.setVisibility(View.GONE);
        }else {
            mTabLayout.setVisibility(View.GONE);
            mTitleView.setVisibility(View.VISIBLE);
        }
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HiResponseCodes.CALENDAR_OK:
                    HiCalendarData calendarData = (HiCalendarData) msg.obj;
                    HiLog.d(TAG, "main2 : "+Thread.currentThread().getId());
                    refreshView(calendarData);
                    break;

                case HiResponseCodes.CALENDAR_FAIL:

                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 刷新view，主线程
     * @param calendarData
     */
    private void refreshView(HiCalendarData calendarData) {
        HiCalendarData.ResultBean.DataBean result = calendarData.getResult().getData();
        String[] date = result.getDate().split("-");
        year.setText(date[0]);
        month.setText(date[1]);
        day1.setText(date[2]);
        weekend.setText(result.getWeekday());

        year_old.setText(result.getLunarYear());
        year_zodiac.setText(result.getAnimalsYear()+"年");
        month_zodiac.setText(result.getLunar());
        day_zodiac.setText(" ");

        day.setText(date[2]);
        suit.setText("宜："+result.getSuit());
        avoio.setText("不适："+result.getAvoid());

        // TODO: 3/11/17  剩下的标签名言名句？
//        words.setText("");
//        author.setText("");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}

