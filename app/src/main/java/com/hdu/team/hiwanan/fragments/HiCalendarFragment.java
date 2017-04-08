package com.hdu.team.hiwanan.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.activity.HiCalendarActivity;
import com.hdu.team.hiwanan.adapter.CusRecyclerViewAdapter;
import com.hdu.team.hiwanan.model.HiCalendar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryYin on 9/21/16.
 */
public class HiCalendarFragment extends Fragment {


    /**
     * Constants
     */
    private View mContentView;
    private Activity mSelf;

    /**Views*/
    private RecyclerView mRecyclerView;
    private ImageView mImageView;

    /**Values*/
    private List<HiCalendar> mCalendarList;
    private CusRecyclerViewAdapter mViewAdapter;


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
            mContentView = inflater.inflate(R.layout.layout_calendar, null);
            initViews();
            initData();
        }
        return mContentView;
    }

    public void initViews() {
        final CardView button;
        button = (CardView) mContentView.findViewById(R.id.btn_test);
        mImageView = (ImageView) mContentView.findViewById(R.id.img_calendar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mSelf, HiCalendarActivity.class);
                Pair<View, String> cardPair = new Pair<View, String>(((View) button), getString(R.string.transition));
                Pair<View, String> imgPair = new Pair<View, String>(((View) mImageView), getString(R.string.transition_img));

//                //普通的  平移，跟我们的overridePendingTransition效果是一样的，从第二个和第三个参数就可以看出
////                ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(mSelf, R.animaation.transition, R.string.transition);
//
//                //场景动画  需要两个activity中的view 去协同完成  一个View
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mSelf, button, getString(R.string.transition));

                //多个View
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mSelf, cardPair, imgPair);

//                //放大   第1个参数是scale哪个view的大小，第2和3个参数是以view为基点，从哪开始动画，这里是该view的中心，4和5参数是新的activity从多大开始放大，这里是从无到有的过程。
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(button, button.getWidth()/2, button.getHeight()/2, 0, 0);

                ActivityCompat.startActivity(mSelf, intent, options.toBundle());


            }
        });


        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.recyclerView_calendar);

    }


    public void initData() {
        // TODO: 10/13/16 add data
        mCalendarList = new ArrayList<>();
        mViewAdapter = new CusRecyclerViewAdapter(mSelf, mCalendarList);
        initCalendars();
    }

    private void initCalendars() {
        for (int i=0; i<10; i++){
            HiCalendar calendar = new HiCalendar();
            calendar.setYear(getString(R.string.year));
            calendar.setMonth(getString(R.string.month));
            calendar.setDay(getString(R.string.day));
            calendar.setWeekend(getString(R.string.weekend));
            calendar.setAuthor(getString(R.string.author));
            calendar.setDay_zodiac(getString(R.string.day_zodiac));
            calendar.setWeekend(getString(R.string.day));
            calendar.setSuit(getString(R.string.suit));
            calendar.setMonth(getString(R.string.month_old));
            calendar.setMonth_zodiac(getString(R.string.month_zodiac));
            calendar.setWords(getString(R.string.words));
            calendar.setYear_old(getString(R.string.year_old));
            calendar.setYear_zodiac(getString(R.string.year_zodiac));
            mCalendarList.add(calendar);
            mViewAdapter.notifyDataSetChanged();
        }

//        String curDate = HiTimesUtil.getCurDate();
//        String url = HiConfig.URL_CALENDAR+"?date="+curDate+"&key="+HiConfig.APP_KEY_CALENDAR;
//        OkHttpUtils.OkHttpGet("www.baidu.com", new OnResponseListener() {
//            @Override
//            public void onSuccess(Object result) {
//
//            }
//
//            @Override
//            public void onFailure(int errorCode, String error) {
//
//            }
//        });

//        List<HiMaps> parems = new ArrayList<>();
//        parems.add(new HiMaps("date", curDate));
//        parems.add(new HiMaps("key", HiConfig.APP_KEY_CALENDAR));
//        OkHttpUtils.OkHttpPost(HiConfig.URL_CALENDAR, parems, new OnResponseListener() {
//            @Override
//            public void onSuccess(Object result) {
//
//            }
//
//            @Override
//            public void onFailure(int errorCode, String error) {
//
//            }
//        });

    }


    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
