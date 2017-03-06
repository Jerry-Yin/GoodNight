package com.hdu.team.hiwanan.fragments;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiFindFragment extends Fragment {

    private static final String TAG = "HiFindFragment";

    /**Constants*/
    private View mContentView;
    private Activity mSelf;


    /**Views*/
    private ViewPager mViewPager;
    private TabLayout mTabLayout;


    /**
     * Values
     */
    private MyPagerFragmentAdapter mPagerAdapter;
    private List<Fragment> mFragmentList;
    private final String[] mTitles = new String[]{"单向历","心情","助睡眠"};



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
        initData();
        Log.d(TAG, "list.size = "+mFragmentList.size());
        mPagerAdapter = new MyPagerFragmentAdapter(getActivity().getSupportFragmentManager(), mSelf, mFragmentList, mTitles);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void initData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new HiCalendarFragment());
        mFragmentList.add(new HiFeelingFragment());
        mFragmentList.add(new HiHelpSleepFragment());
    }




    @Override
    public void onResume() {
        super.onResume();
        mPagerAdapter.notifyDataSetChanged();
        mViewPager.setVisibility(View.VISIBLE);

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
            Log.d(TAG, "adapter.size = "+mFragments.size());
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
