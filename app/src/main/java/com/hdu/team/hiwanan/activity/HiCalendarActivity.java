package com.hdu.team.hiwanan.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.base.HiActivity;
import com.hdu.team.hiwanan.view.PullPushLayout;

/**
 * Created by JerryYin on 9/18/16.
 */
public class HiCalendarActivity extends HiActivity {

    private View btnBack;
    private View btnShare;		//标题分享
    private View navBar;
    private View lineNavBar;

    private PullPushLayout mLayout;
//    private Drawable bgBackDrawable;
//    private Drawable bgShareDrawable;
//    private Drawable bgNavBarDrawable;
//    private Drawable bglineNavBarDrawable;

    private int alphaMax = 180;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test2);
    }


    private void initView() {
//        mLayout = (PullPushLayout) this.findViewById(R.id.layout_);
//        mLayout.setOnTouchEventMoveListenre(new PullPushLayout.OnTouchEventMoveListenre() {
//
//            @Override
//            public void onSlideUp(int mOriginalHeaderHeight, int mHeaderHeight) {
//
//            }
//
//            @Override
//            public void onSlideDwon(int mOriginalHeaderHeight, int mHeaderHeight) {
//
//            }
//
//            @Override
//            public void onSlide(int alpha) {
//                int alphaReverse = alphaMax - alpha;
//                if (alphaReverse < 0) {
//                    alphaReverse = 0;
//                }
////                bgBackDrawable.setAlpha(alphaReverse);
////                bgShareDrawable.setAlpha(alphaReverse);
////                bgNavBarDrawable.setAlpha(alpha);
////                bglineNavBarDrawable.setAlpha(alpha);
//
//            }
//        });
//        navBar = this.findViewById(R.id.nav_bar);
//        lineNavBar = this.findViewById(R.id.line_nav_bar);
//        btnBack = this.findViewById(R.id.iv_back);
//        btnShare = this.findViewById(R.id.iv_share);

//        bgBackDrawable = btnBack.getBackground();
//        bgBackDrawable.setAlpha(alphaMax);
//        bgShareDrawable = btnShare.getBackground();
//        bgShareDrawable.setAlpha(alphaMax);
//
//        bgNavBarDrawable = navBar.getBackground();
//        bglineNavBarDrawable = lineNavBar.getBackground();
//        bgNavBarDrawable.setAlpha(0);
//        bglineNavBarDrawable.setAlpha(0);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAfterTransition(this);
    }
}
