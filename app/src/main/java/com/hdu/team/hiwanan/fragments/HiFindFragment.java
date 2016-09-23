package com.hdu.team.hiwanan.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.activity.TestActivity2;
import com.hdu.team.hiwanan.util.ToastUtils;
import com.hdu.team.hiwanan.view.ScrollerTabView;

import java.util.ArrayList;

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
    private PagerTabStrip mPagerTabStrip;
    private ScrollerTabView mTabView;
    ArrayList<View> mViewContainer = new ArrayList<>();
    ArrayList<String> mTitleContainer = new ArrayList<>();

    View PageCalendar;
    View PageFeeling;
    View PageHelpSleep;

    private MaterialViewPager materialViewPager;

    /**
     * Values
     */
    private HiPagerAdapter mPagerAdapter;
    private HiPageChangeListener mChangeListener;


    public Activity getmSelf(){
        return mSelf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mContentView){
            ViewGroup vg = (ViewGroup) mContentView.getParent();
            if (null != vg){
                vg.removeView(mContentView);
            }
        }else {mSelf = getActivity();
            mContentView = inflater.inflate(R.layout.layout_find, null);
            mSelf = getActivity();
            setupViews();
        }
        return mContentView;
    }

    private void setupViews() {
        mViewPager = (ViewPager) mContentView.findViewById(R.id.view_pager);
        mPagerTabStrip = (PagerTabStrip) mContentView.findViewById(R.id.pager_tab_strip);
        mPagerTabStrip.setDrawFullUnderline(false);
        mPagerTabStrip.setTabIndicatorColorResource(R.color.btn_out_color);
        mPagerTabStrip.setTextSpacing(100);
//        mPagerTabStrip.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
        mPagerTabStrip.setTextSize(1, 16);
        mPagerTabStrip.setTextColor(getResources().getColor(R.color.title_text_color));

        PageCalendar = LayoutInflater.from(mSelf).inflate(R.layout.layout_calendar, null);
        PageFeeling = LayoutInflater.from(mSelf).inflate(R.layout.layout_feeling, null);
        PageHelpSleep = LayoutInflater.from(mSelf).inflate(R.layout.layout_help_sleep, null);
        mViewContainer.add(PageCalendar);
        mViewContainer.add(PageFeeling);
        mViewContainer.add(PageHelpSleep);

        mTitleContainer.add("单向历");
        mTitleContainer.add("心情");
        mTitleContainer.add("助睡眠");

        initDatas();

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }

        });

//        mTabView = (ScrollerTabView) mContentView.findViewById(R.id.scroll_tab_view);
//        mTabView.setTabNum(3);
//        mTabView.setSelectedColor(R.color.colorPrimary, R.color.colorAccent);
    }

    private void initDatas() {
        mPagerAdapter = new HiPagerAdapter();
        mChangeListener = new HiPageChangeListener();

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(mChangeListener);

    }





    class HiPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mViewContainer.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(mViewContainer.get(position));
//            super.destroyItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewContainer.get(position));
            return mViewContainer.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleContainer.get(position);
//            return super.getPageTitle(position);
        }

    }

    class HiPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.d(TAG, "-------scrolled position:" + position);
            Log.d(TAG, "-------scrolled positionOffset:" + positionOffset);
            Log.d(TAG, "-------scrolled positionOffsetPixels:" + positionOffsetPixels);
//            mTabView.setOffset(position, positionOffset);

        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "------selected:" + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d(TAG, "--------changed:" + state);
        }
    }



//    class HiViewPager extends ViewPager{
//
//        public HiViewPager(Context context) {
//            super(context);
//        }
//
//        @Override
//        public boolean dispatchTouchEvent(MotionEvent ev) {
//            boolean ret = super.dispatchTouchEvent(ev);
//            if(ret)
//            {
//                ((ViewGroup)itemViewPager.getParent()).requestDisallowInterceptTouchEvent(true);
//            }
//            return ret;
////            return super.dispatchTouchEvent(ev);
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();

//        materialViewPager = (MaterialViewPager) PageCalendar.findViewById(R.id.material_viewpager);
        final CardView button;
        button = (CardView) PageCalendar.findViewById(R.id.btn_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(mSelf, "jajajaj", Toast.LENGTH_SHORT);

                Intent intent = new Intent(mSelf, TestActivity2.class);

                //普通的  平移，跟我们的overridePendingTransition效果是一样的，从第二个和第三个参数就可以看出
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(mSelf, R.animaation.transition, R.string.transition);

                //场景动画  需要两个activity中的view 去协同完成
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mSelf, button, getString(R.string.transition));

                //放大   第1个参数是scale哪个view的大小，第2和3个参数是以view为基点，从哪开始动画，这里是该view的中心，4和5参数是新的activity从多大开始放大，这里是从无到有的过程。
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(button, button.getWidth()/2, button.getHeight()/2, 0, 0);


                ActivityCompat.startActivity(mSelf, intent, options.toBundle());
//                startActivity(new Intent(mSelf, TestActivity2.class));

            }
        });
    }
}
