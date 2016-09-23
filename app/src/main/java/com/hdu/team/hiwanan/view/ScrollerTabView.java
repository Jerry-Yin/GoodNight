package com.hdu.team.hiwanan.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by JerryYin on 9/20/16.
 *
 * ViewPager 自定义滑动tab
 */
public class ScrollerTabView extends View {


    private int mTabNum, mCurrentNum;
    private float mWidth, mTabWidth, mOffset;
    private Paint mPaint;
    private int mBeginColor;
    private int mEndColor;

    /**
     * 颜色渐变
     * 它除了定义开始颜色和结束颜色以外还可以定义，多种颜色组成的分段渐变效果
     * LinearGradient shader = new LinearGradient(0, 0, endX, endY, new int[]{startColor, midleColor, endColor},new float[]{0 , 0.5f, 1.0f}, TileMode.MIRROR);
     * 其中参数new int[]{startColor, midleColor, endColor}是参与渐变效果的颜色集合，
     * 其中参数new float[]{0 , 0.5f, 1.0f}是定义每个颜色处于的渐变相对位置，
     * 这个参数可以为null，如果为null表示所有的颜色按顺序均匀的分布
     */
    LinearGradient mGradient;


    public ScrollerTabView(Context context) {
        super(context);
        initView();
    }

    public ScrollerTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {
        if (mPaint == null){
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

    }

    public void setTabNum(int tabNum) {
        this.mTabNum = tabNum;
    }

    public void setCurrentNum(int currentNum) {
        this.mCurrentNum = currentNum;
    }

    public void setOffset(int position, float offset) {
        if (offset == 0){
            return;
        }
        mCurrentNum = position;
        this.mOffset = offset;
        invalidate();
    }

    public void setSelectedColor(int color1, int color2){
        mBeginColor = color1;
        mEndColor = color2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTabWidth == 0){
            mWidth = getWidth();
            mTabWidth = mWidth / mTabNum;
        }
        //根据位置和偏移量来计算滑条位置
        float left = (mCurrentNum + mOffset) * mTabWidth;
        final float right = left + mTabWidth;
        final float top = getPaddingTop();
        final float bottom = getHeight() - getPaddingBottom();

        if (mGradient == null){
            mGradient = new LinearGradient(left, getHeight(), right, getHeight(), mBeginColor, mEndColor, Shader.TileMode.CLAMP);
        }
        mPaint.setShader(mGradient);

        canvas.drawRect(left, top, right, bottom, mPaint);

    }
}
