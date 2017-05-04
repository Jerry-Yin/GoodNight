package com.hdu.team.hiwanan.util.common;

import android.view.MotionEvent;
import android.view.View;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.util.HiLog;

/**
 * Created by JerryYin on 4/28/17.
 * 手势滑动类
 */

public class HiGestureUtil {


    private float startX = 0;
    private float startY = 0;
    private float endX = 0;
    private float endY = 0;

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (v.getId()) {
//            case R.id.layout_countdown:
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
////                        mScrollView.requestDisallowInterceptTouchEvent(false);
//                        HiLog.d(TAG, "滑down "+ (endY-startY));
//                        startX = event.getX();
//                        startY = event.getY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        HiLog.d(TAG, "滑move "+ (endY-startY));
////                        mScrollView.requestDisallowInterceptTouchEvent(false);
//                        endX = event.getX();
//                        endY = event.getY();
//                        if (endY - startY < 0) {
//                            //下滑
//// && Math.abs(endY - startY) > 5f
////                            mScrollView.requestDisallowInterceptTouchEvent(false);
//                            HiLog.d(TAG, "下滑 "+ (endY-startY));
//                            if (mLayoutExpend.getVisibility() == View.GONE) {
//                                animateOpen(mLayoutExpend);
//                                animationIvOpen();
//                            } else {
//                                animateClose(mLayoutExpend);
//                                animationIvClose();
//                            }
//                        } else if (endY - startY > 0 ) {
//                            //上滑
////                            mScrollView.requestDisallowInterceptTouchEvent(false);
//                            HiLog.d(TAG, "上滑 "+ (endY-startY));
//                            if (mLayoutExpend.getVisibility() == View.GONE) {
//                                animateOpen(mLayoutExpend);
//                                animationIvOpen();
//                            } else {
//                                animateClose(mLayoutExpend);
//                                animationIvClose();
//                            }
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        HiLog.d(TAG, "滑up 1 "+ (startY));
//                        HiLog.d(TAG, "滑up 2 "+ endY);
//                        HiLog.d(TAG, "滑up 3 "+ event.getY());
//                        mScrollView.requestDisallowInterceptTouchEvent(false);
//
//                        break;
//                }
//
//                break;
//        }
//        return false;
//    }

}
