package com.hdu.team.hiwanan.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.base.HiActivity;

/**
 * Created by JerryYin on 11/16/15.
 */
public class HiLogoActivity extends HiActivity implements View.OnClickListener {


    private ImageView mImg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.layout_logo, null);
        setContentView(view);

        initView();

        setAnimation(view);

    }

    private void initView() {
        mImg1 = (ImageView) findViewById(R.id.image1);
    }

    private void setAnimation(View view) {
        //透明度渐变
//        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
//        alphaAnimation.setDuration(2000);
//        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                goToApplication();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
////        view.startAnimation(alphaAnimation);
//
//        /**
//         * 缩放动画
//         *
//         float fromX 动画起始时 X坐标上的伸缩尺寸
//         float toX 动画结束时 X坐标上的伸缩尺寸
//         float fromY 动画起始时Y坐标上的伸缩尺寸
//         float toY 动画结束时Y坐标上的伸缩尺寸
//         int pivotXType 动画在X轴相对于物件位置类型
//         float pivotXValue 动画相对于物件的X坐标的开始位置
//         int pivotYType 动画在Y轴相对于物件位置类型
//         float pivotYValue 动画相对于物件的Y坐标的开始位置
//         */
//        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//
//        scaleAnimation.setRepeatCount(0);//设置重复次数
//        scaleAnimation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
////        scaleAnimation.setStartOffset( long startOffset);//执行前的等待时间
//        scaleAnimation.setDuration(2000);
////        view.startAnimation(scaleAnimation);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
//                ObjectAnimator.ofFloat(view, "rotationX", 0, 360),    // 旋转
//                ObjectAnimator.ofFloat(myView, "rotationY", 0, 180),
//                ObjectAnimator.ofFloat(myView, "rotation", 0, -90),
//                ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1.4f, 0.0f, 1.4f,
//                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f),    //缩放X
//                ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1.4f, 0.0f, 1.4f,
//                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f),    //缩放Y
//                ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f)   //透明度

                ObjectAnimator.ofFloat(mImg1, "scaleX", 1.5f, 0.0f),    //缩放X
                ObjectAnimator.ofFloat(mImg1, "scaleY", 1.5f, 0.0f),    //缩放Y
                ObjectAnimator.ofFloat(mImg1, "alpha", 1.0f, 0.1f)   //透明度
        );

        set.setDuration(2000).start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                goToApplication();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    private void goToApplication() {
        Intent intent = new Intent(this, HiLoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {

    }
}
