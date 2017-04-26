package com.hdu.team.hiwanan.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.activity.HiCollectionActivity;
import com.hdu.team.hiwanan.activity.HiWanAnActivity;
import com.hdu.team.hiwanan.listener.OnResponseListener;
import com.hdu.team.hiwanan.manager.HiDialogManager;
import com.hdu.team.hiwanan.manager.HiMediaPlayerManager;
import com.hdu.team.hiwanan.model.RecorderVoice;
import com.hdu.team.hiwanan.util.HiLog;
import com.hdu.team.hiwanan.util.HiToast;
import com.hdu.team.hiwanan.util.HiUploadAudioUtil;
import com.hdu.team.hiwanan.util.common.HiTimesUtil;
import com.hdu.team.hiwanan.view.HiVoiceRecorderButton2;

import java.io.File;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiGoodNightFragment2 extends Fragment implements View.OnClickListener, HiVoiceRecorderButton2.OnFinishRecorderListener {

    /**
     * Values
     */
    private static final String TAG = "HiGoodNightFragment";

    /**
     * Constants
     */
    private View mContentView;
    private Activity mSelf;

    /**
     * Views
     */
    private HiVoiceRecorderButton2 mBtnSpeak;
    //    private RadioButton mbtnCollection;
//    private RadioButton mbtnHelpSleep;
    private LinearLayout mLayoutExpend;
    private ImageView mImgExpend;

    private float mDensity;
    private int mHiddenViewMeasuredHeight;  //ExpendLayout 的高度


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
            mContentView = inflater.inflate(R.layout.layout_goodnight2, null);
            initViews();
            initData();
        }
        return mContentView;
    }

    public void initViews() {
        mBtnSpeak = (HiVoiceRecorderButton2) mContentView.findViewById(R.id.btn_speak);
        mLayoutExpend = (LinearLayout) mContentView.findViewById(R.id.layout_expend);
        mImgExpend = (ImageView) mContentView.findViewById(R.id.img_expend);
        mDensity = mContentView.getResources().getDisplayMetrics().density;
        mHiddenViewMeasuredHeight = (int) (mDensity * 120 + 0.5);
        Log.d(TAG, mHiddenViewMeasuredHeight + "");

        mImgExpend.setOnClickListener(this);
        mBtnSpeak.setOnClickListener(this);
        mBtnSpeak.setAudioFinishRecorderListener(this);
    }

    public void initData() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_speak:

                break;

            case R.id.img_expend:
//                expendView
                if (mLayoutExpend.getVisibility() == View.GONE) {
                    animateOpen(mLayoutExpend);
                    animationIvOpen();
                } else {
                    animateClose(mLayoutExpend);
                    animationIvClose();
                }
                break;
            default:
                break;
        }
    }


    private void animateOpen(View v) {
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(v, 0,
                mHiddenViewMeasuredHeight);
        animator.start();
    }

    private void animationIvOpen() {
        RotateAnimation animation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(100);
        mImgExpend.startAnimation(animation);
    }


    private void animationIvClose() {
        RotateAnimation animation = new RotateAnimation(180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(100);
        mImgExpend.startAnimation(animation);
    }

    private void animateClose(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

        });
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        HiLog.d(TAG, v.getVisibility() + "");
        HiLog.d(TAG, "view:" + v + " start:" + start + " end:" + end);
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);

            }
        });
        return animator;
    }


    /**
     * on AudioFinishRecorderListener
     *
     * @param times
     * @param filePath
     */
    @Override
    public void onFinish(float times, final String filePath) {
        RecorderVoice recorderVoice = new RecorderVoice(filePath, times, HiTimesUtil.getCurDateTime());

        final AlertDialog.Builder builder = new AlertDialog.Builder(mSelf);
        builder.setTitle("亲，确定要发送吗？")
                .setIcon(R.drawable.voice5)
                .setItems(new String[]{"点我试听哦"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        HiMediaPlayerManager.playSound(filePath, new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                //TODO 播放完成后调用
//                mAnimView.setBackgroundResource(R.drawable.img_anim);
                                builder.show();
                            }
                        });
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File audio = new File(filePath);
                        if (audio.exists()) {
                            HiUploadAudioUtil.upLoadAudio(audio, "", new OnResponseListener() {
                                @Override
                                public void onSuccess(Object result) {
                                    HiToast.showToast(mSelf, result.toString());
                                }

                                @Override
                                public void onFailure(int errorCode, String error) {
                                    HiToast.showToast(mSelf, errorCode + error);
                                }
                            });
                        } else {
                            HiToast.showToast(mSelf, filePath + " 文件不存在!");
                        }
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}
