package com.hdu.team.hiwanan.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.base.HiActivity;
import com.hdu.team.hiwanan.util.ToastUtils;

/**
 * Created by JerryYin on 9/18/16.
 */
public class HiCalendarActivity extends HiActivity {


    private static final String TAG = "HiCalendarActivity";
    private MaterialViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "HiCalendarActivity.onCreate() is called..");
        setContentView(R.layout.layout_calendar);
//        mViewPager = (MaterialViewPager) findViewById(R.id.material_viewpager);
//        initViews();
//        mViewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_MOVE:
//                        ((ViewParent) v.getParent()).requestDisallowInterceptTouchEvent(true);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        ((ViewParent) v.getParent()).requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//                return true;
//            }
//        });
    }

    private void initViews() {

    }


    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.btn_test)
//            ToastUtils.showToast(this, "jajajaj", Toast.LENGTH_SHORT);
//        Log.d(TAG, "JAJAJAJJA");
    }

    public void test(View v){

    }
}
