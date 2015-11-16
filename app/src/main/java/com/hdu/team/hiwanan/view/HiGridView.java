package com.hdu.team.hiwanan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by JerryYin on 11/13/15.
 * 自定义不可滑动的 GridView
 */
public class HiGridView extends GridView {

    public HiGridView(Context context) {
        super(context);
    }

    public HiGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}
