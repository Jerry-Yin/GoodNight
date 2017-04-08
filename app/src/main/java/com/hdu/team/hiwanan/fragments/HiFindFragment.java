package com.hdu.team.hiwanan.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.util.HiLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiFindFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private static final String TAG = "HiFindFragment";

    /**
     * Constants
     */
    private View mContentView;
    private Activity mSelf;


    /**
     * Views
     */
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private View mTabShare;

    private HiCalendarFragment2 mCalendarFragment;
    private HiFeelingFragment mFeelingFragment;
    private HiHelpSleepFragment mSleepFragment;

    /**
     * Values
     */
    private MyPagerFragmentAdapter mPagerAdapter;
    private List<Fragment> mFragmentList;
    private final String[] mTitles = new String[]{"日历", "心情", "助睡眠"};


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
            mContentView = inflater.inflate(R.layout.layout_find, null);
            initViews();

        }
        return mContentView;
    }


    public void initViews() {
        mViewPager = (ViewPager) mContentView.findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) mContentView.findViewById(R.id.tab_layout);
        mTabShare = mContentView.findViewById(R.id.title_view_share);
        initData();
        Log.d(TAG, "list.size = " + mFragmentList.size());
        mPagerAdapter = new MyPagerFragmentAdapter(getActivity().getSupportFragmentManager(), mSelf, mFragmentList, mTitles);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void initData() {
        mFragmentList = new ArrayList<>();
        if (mCalendarFragment == null)
            mCalendarFragment = new HiCalendarFragment2();
        if (mFeelingFragment == null)
            mFeelingFragment = new HiFeelingFragment();
        if (mSleepFragment == null)
            mSleepFragment = new HiHelpSleepFragment();
        if (!mFragmentList.contains(mCalendarFragment))
            mFragmentList.add(mCalendarFragment);
        if (!mFragmentList.contains(mFeelingFragment))
            mFragmentList.add(mFeelingFragment);
        if (!mFragmentList.contains(mSleepFragment))
            mFragmentList.add(mSleepFragment);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        mPagerAdapter.notifyDataSetChanged();
        mViewPager.setVisibility(View.VISIBLE);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        HiLog.d(TAG, "sel : " + position);
        switch (position) {
            case 0:
                if (mCalendarFragment.mWhichTab == 0) {
                    mTabShare.setVisibility(View.GONE);
                    mTabLayout.setVisibility(View.VISIBLE);
                }else if (mCalendarFragment.mWhichTab == 1){
                    mTabShare.setVisibility(View.VISIBLE);
                    mTabLayout.setVisibility(View.GONE);
                }
                break;

            case 1:
                mTabShare.setVisibility(View.GONE);
                mTabLayout.setVisibility(View.VISIBLE);
                mTabLayout.setBackgroundColor(getResources().getColor(R.color.content_color));
                break;

            case 2:
                mTabShare.setVisibility(View.GONE);
                mTabLayout.setVisibility(View.VISIBLE);
                mTabLayout.setBackgroundColor(getResources().getColor(R.color.content_color));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    /**
     * 基础版本适配器
     */
    public class MyPagerFragmentAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private List<android.support.v4.app.Fragment> mFragments;
        private String[] mTitles;

        public MyPagerFragmentAdapter(FragmentManager fm, Context context, List<android.support.v4.app.Fragment> fragments, String[] titles) {
            super(fm);
            this.mContext = context;
            this.mFragments = fragments;
            this.mTitles = titles;
            Log.d(TAG, "adapter.size = " + mFragments.size());
//            Log.d(TAG, "adapter.size = "+mFragmentList.size());
//            Log.d(TAG, "adapter.size = "+mFragmentList.size());

        }


        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }


}
