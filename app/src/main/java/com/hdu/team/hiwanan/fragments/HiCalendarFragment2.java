package com.hdu.team.hiwanan.fragments;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.adapter.CusRecyclerViewAdapter;
import com.hdu.team.hiwanan.constant.HiResponseCodes;
import com.hdu.team.hiwanan.model.HiCalendar;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.listener.OnResponseListener;
import com.hdu.team.hiwanan.model.HiCalendarData;
import com.hdu.team.hiwanan.model.bmob.Calendar;
import com.hdu.team.hiwanan.model.bmob.Comment;
import com.hdu.team.hiwanan.model.bmob.UserBmob;
import com.hdu.team.hiwanan.network.BmobNetworkUtils;
import com.hdu.team.hiwanan.util.HiLog;
import com.hdu.team.hiwanan.util.ImageLoaderUtil;
import com.hdu.team.hiwanan.util.OkHttpUtils;
import com.hdu.team.hiwanan.util.common.GsonUtils;
import com.hdu.team.hiwanan.util.common.HiTimesUtil;
import com.hdu.team.hiwanan.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


/**
 * Created by JerryYin on 3/9/17.
 */

public class HiCalendarFragment2 extends Fragment implements View.OnClickListener {

    /**
     * Constants
     */
    private View mContentView;
    private static final java.lang.String TAG = "HiCalendarFragment2";
    private Activity mSelf;
    private SharedPreferences mSharedPreferences;

    /**
     * Views
     */
//    private RecyclerView mRecyclerView;
//    private ImageView mImageView;
    private ScrollView mScrollView;
    private TabLayout mTabLayout;
    private View mTitleView;
    private ImageView mBtnShare;

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

    private TextView likeSum; //点赞总数
    private TextView commentSum; // 评论总数
    private TextView shareSum; //分享总数
    private LinearLayout likeSummay;

    private Calendar mCalendarObj; //用于更新calendar数据

    private RecyclerView mRecyclerView;
    private List<Comment> mCommentDataList = new ArrayList<>();
    private RecyclerViewAdapter mRecyclerViewAdapter;


    /**
     * Values
     */
    private HiCalendar mCalendar;
    private CusRecyclerViewAdapter mViewAdapter;

    private float mDistanceY;   //滑动高度
    private int mTabHeight;     //标题栏高度
    public int mWhichTab = 0;   //标识位，确定该显示哪个标题
    private String mToday = HiTimesUtil.getCurDate();
    private boolean mThumbed = false; //是否对日历点过赞


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
            mSharedPreferences = mSelf.getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, Context.MODE_PRIVATE);
            mThumbed = mSharedPreferences.getBoolean(mToday, false);
            mContentView = inflater.inflate(R.layout.layout_calendar2, null);
            initViews();

            initData();
            showWhichTab();
            HiLog.d(TAG, "main : " + Thread.currentThread().getId());
        }
        return mContentView;
    }

    public void initViews() {
        mScrollView = (ScrollView) mContentView.findViewById(R.id.sv_calendar);
        mTitleView = mSelf.findViewById(R.id.title_view_share);
        mBtnShare = (ImageView) mSelf.findViewById(R.id.btn_title_share);
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

        //calendar comment summary
        likeSum = (TextView) mContentView.findViewById(R.id.thumb_summary);
        likeSummay = (LinearLayout) mContentView.findViewById(R.id.linear_thumb);

        if (mThumbed) {
            likeSum.setTextColor(Color.RED);
        }
        likeSum.setOnClickListener(this);
        likeSummay.setOnClickListener(this);
        commentSum = (TextView) mContentView.findViewById(R.id.comment_summary);
        shareSum = (TextView) mContentView.findViewById(R.id.share_summary);

        mBtnShare.setOnClickListener(this);

        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.rcy_comment);
        mRecyclerViewAdapter = new RecyclerViewAdapter(mSelf, mCommentDataList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mSelf));

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void initData() {
        // TODO: 10/13/16 add data
        mTabHeight = mScrollView.getBottom();
        initCalendars();
        initScroll();
        initComments();
    }

    /**
     * 查询今天所有的评论数据
     * yyyy-mm-dd
     */
    private void initComments() {
        String today = HiTimesUtil.getCurDate();
        BmobNetworkUtils.queryComment(BmobNetworkUtils.KEY_DATE, today, new OnResponseListener<List<Comment>>() {
            @Override
            public void onSuccess(List<Comment> result) {
                if (result != null)
                    handMessage(mHandler, HiResponseCodes.COMMENT_OK, result);
            }

            @Override
            public void onFailure(int errorCode, String error) {
                handMessage(mHandler, HiResponseCodes.COMMENT_NO, errorCode + error);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initScroll() {
        mTabLayout = (TabLayout) mSelf.findViewById(R.id.tab_layout);
        day_zodiac = (TextView) mContentView.findViewById(R.id.text_day_zodiac);
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                HiLog.d(TAG, "scrollY: " + scrollY);
                HiLog.d(TAG, "oldY: " + oldScrollY);
//                mDistanceY += scrollY > oldScrollY ? scrollY-oldScrollY : 0;
                mDistanceY = scrollY;
                int titleHeight = mTabLayout.getBottom();
                HiLog.d(TAG, "height: " + titleHeight);
                HiLog.d(TAG, "distance: " + mDistanceY);

                /**
                 * 绿色：#00BFA5
                 * 三原色：(0 - 191 - 165) (可通过PS获取)
                 * 高度没到达时候。渐变
                 * 白色：#ffffff (255 - 255 - 255)
                 */
                if (mDistanceY == 0) {
                    mTitleView.setVisibility(View.GONE);
                    mTabLayout.setVisibility(View.VISIBLE);
                    mWhichTab = 0;
                }
                if (mDistanceY <= titleHeight) {
                    HiLog.d(TAG, "distance: " + mDistanceY);
                    float scale = (float) mDistanceY / titleHeight;
                    float alpha = scale * 255;
                    HiLog.d(TAG, "scale: " + scale);
                    HiLog.d(TAG, "alpha: " + alpha);
                    HiLog.d(TAG, "---------------------------");
                    mTabLayout.setBackgroundColor(Color.argb((int) alpha, 0, 191, 165));
                    mTitleView.setBackgroundColor(Color.argb((int) alpha, 0, 191, 165));
                } else {
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
        String url = HiConfig.URL_CALENDAR + "?date=" + date + "&key=" + HiConfig.APP_KEY_CALENDAR;
        OkHttpUtils.OkHttpGet(url, new OnResponseListener() {
            @Override
            public void onSuccess(Object result) {
                String data = GsonUtils.parseJsonToString(result);
                HiLog.d(TAG, "result : " + result.toString());
                HiLog.d(TAG, "success : " + data);
//                HiCalendarData calendarData = GsonUtils.parseJsonToClass(data, HiCalendarData.class);
                Gson gson = new Gson();
//                String d = gson.toJson(result);

                HiCalendarData calendarData = gson.fromJson(String.valueOf(result), HiCalendarData.class);
                Message message = new Message();
                message.what = HiResponseCodes.CALENDAR_OK;
                message.obj = calendarData;
                HiLog.d(TAG, "network : " + Thread.currentThread().getId());
                mHandler.sendMessage(message);

            }

            @Override
            public void onFailure(int errorCode, String error) {
                Message message = new Message();
                message.what = HiResponseCodes.CALENDAR_FAIL;
                message.obj = errorCode + error;
                mHandler.sendMessage(message);
                HiLog.d(TAG, "error " + errorCode + " " + error);
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
        BmobNetworkUtils.queryCalendar(today, new OnResponseListener<List<Calendar>>() {
            @Override
            public void onSuccess(List<Calendar> result) {
                if (result != null && result.size() != 0) {
                    handMessage(mHandler, HiResponseCodes.SUMMAY_OK, result.get(0));
                }
                HiLog.d(TAG, result.toString());
            }

            @Override
            public void onFailure(int errorCode, String error) {
                handMessage(mHandler, HiResponseCodes.SUMMAY_FAIL, errorCode + error);
                HiLog.d(TAG, errorCode + error);
            }
        });
    }

    private void showWhichTab() {
//        if (mTitleView != null && mTabLayout != null) {
        if (mDistanceY < mTabHeight) {
            mTabLayout.setVisibility(View.VISIBLE);
            mTitleView.setVisibility(View.GONE);
        } else {
            mTabLayout.setVisibility(View.GONE);
            mTitleView.setVisibility(View.VISIBLE);
        }
//        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HiResponseCodes.CALENDAR_OK:
                    HiCalendarData calendarData = (HiCalendarData) msg.obj;
                    HiLog.d(TAG, "main2 : " + Thread.currentThread().getId());
                    refreshView(calendarData);
                    break;

                case HiResponseCodes.CALENDAR_FAIL:

                    break;

                case HiResponseCodes.SUMMAY_OK:
                    Calendar calendar = (Calendar) msg.obj;
                    //HiLog.e("Kaikai" + calendar.getLike());
                    if (calendar != null) {
                        mCalendarObj = calendar;
                        setSummary(calendar);
                    }
                    break;

                case HiResponseCodes.SUMMAY_FAIL:
                    break;

                case HiResponseCodes.COMMENT_OK:
                    List<Comment> list = (List<Comment>) msg.obj;
                    mCommentDataList.clear();
                    for (Comment c : list) {
                        mCommentDataList.add(c);
                    }
                    mRecyclerViewAdapter.notifyDataSetChanged();
                    break;

                case HiResponseCodes.COMMENT_NO:

                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 刷新view，主线程
     *
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
        year_zodiac.setText(result.getAnimalsYear() + "年");
        month_zodiac.setText(result.getLunar());
        day_zodiac.setText(" ");

        day.setText(date[2]);
        suit.setText("宜：" + result.getSuit());
        avoio.setText("不适：" + result.getAvoid());

        // TODO: 3/11/17  剩下的标签名言名句？
//        words.setText("");
//        author.setText("");
    }

    private void setSummary(Calendar calendar) {
        likeSum.setText(String.valueOf(calendar.getLike()));
        commentSum.setText(String.valueOf(calendar.getComment()));
        shareSum.setText(String.valueOf(calendar.getShare()));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> {

        private Context mContext;
        private List<Comment> mCommentList;
        private Handler mCommentHandler;

        public RecyclerViewAdapter(Context mContext, List<Comment> mCommentList) {
            this.mContext = mContext;
            this.mCommentList = mCommentList;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_comment, parent, false);
            CustomViewHolder holder = new CustomViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder holder, int position) {
            mCommentHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case HiResponseCodes.USER_BMOB_OK:
                            HiLog.d(TAG, "holder2 : " + holder.toString());
                            UserBmob user = (UserBmob) msg.obj;
                            ImageLoaderUtil.displayDrawableImage(user.getIcon(), holder.img_icon);
                            HiLog.d(TAG, "nick name : " + user.getUsername());
//                            holder.tv_nick_name.setText(user.getUsername());
                            break;

                        case HiResponseCodes.USER_BMOB_NO:

                            break;

                        case HiResponseCodes.COMMENT_OK:
                            HiLog.d(TAG, "holder3 : " + holder.toString());
                            Comment c = (Comment) msg.obj;
                            HiLog.d(TAG, "words2 : " + c.getWords());
                            holder.tv_word2.setText(c.getWords());
                            break;

                        case HiResponseCodes.COMMENT_NO:

                            break;

                    }
                }
            };

            Comment comment = mCommentList.get(position);
            holder.tv_nick_name.setText(comment.getUser());
            String userId = comment.getUserid();
            HiLog.d(TAG, "holder1 : " + holder.toString());
            BmobNetworkUtils.queryUserBmob(BmobNetworkUtils.KEY_OBJECT_ID, userId, new OnResponseListener<List<UserBmob>>() {
                @Override
                public void onSuccess(List<UserBmob> result) {
                    if (result != null && result.size() > 0) {
                        handMessage(mCommentHandler, HiResponseCodes.USER_BMOB_OK, result.get(0));
                    }
                }

                @Override
                public void onFailure(int errorCode, String error) {
                    handMessage(mCommentHandler, HiResponseCodes.USER_BMOB_NO, errorCode + "|" + error);
                }
            });

            String[] now = HiTimesUtil.getCurTime().split(":");             //hh:mm
            String[] time = comment.getTime().split(":");     //hh:mm:ss
            if (HiTimesUtil.isjustNow(now, time)) {
                holder.tv_time.setText("刚刚");
            } else {
                holder.tv_time.setText(time[0] + "." + time[1]);
            }

            holder.tv_like.setText(String.valueOf(comment.getLike()));
            holder.tv_word1.setText(comment.getWords());
            if (comment.getLastid() < 0) {
                //单条评论
                holder.tv_reply.setVisibility(View.GONE);
                holder.tv_last_usr1.setVisibility(View.GONE);
                holder.tv_signal1.setVisibility(View.GONE);
                holder.layout_sub.setVisibility(View.GONE);
            } else {
                holder.tv_reply.setVisibility(View.VISIBLE);
                holder.tv_last_usr1.setVisibility(View.VISIBLE);
                holder.tv_signal1.setVisibility(View.VISIBLE);
                holder.layout_sub.setVisibility(View.VISIBLE);

                holder.tv_last_usr1.setText(comment.getLastName());
                holder.tv_last_usr2.setText(comment.getLastName());

                BmobNetworkUtils.queryComment(BmobNetworkUtils.KEY_ID, comment.getLastid(), new OnResponseListener<List<Comment>>() {
                    @Override
                    public void onSuccess(List<Comment> result) {
                        if (result != null && result.size() > 0) {
                            handMessage(mCommentHandler, HiResponseCodes.COMMENT_OK, result.get(0));
                        }
                    }

                    @Override
                    public void onFailure(int errorCode, String error) {
                        handMessage(mCommentHandler, HiResponseCodes.COMMENT_NO, errorCode + "|" + error);
                    }
                });
            }


        }

        @Override
        public int getItemCount() {
            return mCommentList.size();
        }

        //CustomViewHolder，用于缓存，提高效率
        public class CustomViewHolder extends RecyclerView.ViewHolder {
            private RoundImageView img_icon;
            private TextView tv_nick_name;
            private TextView tv_time;
            private TextView tv_like;
            private TextView tv_reply;
            private TextView tv_last_usr1;
            private TextView tv_signal1;
            private TextView tv_word1;

            private LinearLayout layout_sub;
            private TextView tv_last_usr2;
            private TextView tv_signal2;
            private TextView tv_word2;

            public CustomViewHolder(View itemView) {
                super(itemView);
                img_icon = (RoundImageView) itemView.findViewById(R.id.img_user);
                tv_nick_name = (TextView) itemView.findViewById(R.id.txt_nick_name);
                tv_time = (TextView) itemView.findViewById(R.id.txt_comment_time);
                tv_like = (TextView) itemView.findViewById(R.id.txt_like);
                tv_reply = (TextView) itemView.findViewById(R.id.txt_reply);
                tv_last_usr1 = (TextView) itemView.findViewById(R.id.txt_last_user1);
                tv_signal1 = (TextView) itemView.findViewById(R.id.txt_signal);
                tv_word1 = (TextView) itemView.findViewById(R.id.txt_word1);
                layout_sub = (LinearLayout) itemView.findViewById(R.id.layout_comment_sub);
                tv_last_usr2 = (TextView) itemView.findViewById(R.id.txt_last_user2);
                tv_signal2 = (TextView) itemView.findViewById(R.id.txt_signal2);
                tv_word2 = (TextView) itemView.findViewById(R.id.txt_word2);
            }
        }
    }


    public <T> void handMessage(Handler handler, int what, T result) {
        Message message = new Message();
        message.what = what;
        message.obj = result;
        handler.sendMessage(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.thumb_summary:
                thumbClicked();
                break;
            case R.id.linear_thumb:
                thumbClicked();
                break;

            case R.id.btn_title_share:
                showShareUI();
                break;
            default:
                break;
        }
    }

    private void showShareUI() {
        ShareSDK.initSDK(mSelf);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
//        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("Hi晚安");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("https://github.com/Jerry-Yin/GoodNight");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我想跟你说'晚安'哦！");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://hiwanan.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("https://github.com/Jerry-Yin/GoodNight");

        // 启动分享GUI
        oks.show(mSelf);
    }

    /**
     * 点赞操作
     */
    private void thumbClicked() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (mThumbed == false) {
            int origin = Integer.valueOf(likeSum.getText().toString());
            origin++;
            likeSum.setText(String.valueOf(origin));
            likeSum.setTextColor(Color.RED);
            editor.putBoolean(mToday, true);
            editor.commit();
            mThumbed = true;
            HiLog.e(mCalendarObj.getObjectId());
            mCalendarObj.setLike(origin);
            boolean success = BmobNetworkUtils.updateCalendar(mCalendarObj);
            HiLog.e(success + "");

        }
    }
}

