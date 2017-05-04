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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.activity.HiMusicPlayerActivity;
import com.hdu.team.hiwanan.listener.OnPlayingListener;
import com.hdu.team.hiwanan.listener.OnResponseListener;
import com.hdu.team.hiwanan.manager.HiMediaPlayerManager;
import com.hdu.team.hiwanan.model.RecorderVoice;
import com.hdu.team.hiwanan.util.HiLog;
import com.hdu.team.hiwanan.util.HiToast;
import com.hdu.team.hiwanan.util.HiUploadAudioUtil;
import com.hdu.team.hiwanan.util.common.HiTimesUtil;
import com.hdu.team.hiwanan.view.HiCountdownLinearLayout;
import com.hdu.team.hiwanan.view.HiVoiceRecorderButton2;

import java.io.File;

import co.mobiwise.library.MusicPlayerView;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiGoodNightFragment2 extends Fragment implements View.OnClickListener, HiVoiceRecorderButton2.OnFinishRecorderListener, GestureDetector.OnGestureListener, View.OnTouchListener {


    /**
     * Constants
     */
    private static final String TAG = "HiGoodNightFragment";


    /**
     * Views
     */
    private View mContentView;
    private Activity mSelf;
    private HiVoiceRecorderButton2 mBtnSpeak;
    //    private RadioButton mbtnCollection;
//    private RadioButton mbtnHelpSleep;
    private LinearLayout mLayoutExpend;
    private ImageView mImgExpend;

    private LinearLayout mMusicLayout;
    private ScrollView mScrollView;
    private HiCountdownLinearLayout mLayoutCountdown;
    private MusicPlayerView mMusicView;


    /**
     * Values
     */
    private float mDensity;
    private int mHiddenViewMeasuredHeight;  //ExpendLayout 的高度
    private HiMediaPlayerManager mPlayerManager;

    private GestureDetector mDetector;


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
        mScrollView = (ScrollView) mContentView.findViewById(R.id.scroll_layout);
        mLayoutCountdown = (HiCountdownLinearLayout) mContentView.findViewById(R.id.layout_countdown);
        mLayoutExpend = (LinearLayout) mContentView.findViewById(R.id.layout_expend);
        mImgExpend = (ImageView) mContentView.findViewById(R.id.img_expend);
        mDensity = mContentView.getResources().getDisplayMetrics().density;
        mHiddenViewMeasuredHeight = (int) (mDensity * 120 + 0.5);
        Log.d(TAG, mHiddenViewMeasuredHeight + "");
        mMusicLayout = (LinearLayout) mContentView.findViewById(R.id.music_layout);
        mMusicView = (MusicPlayerView) mContentView.findViewById(R.id.music_view);

        mMusicView.setOnClickListener(this);
        mLayoutCountdown.setOnTouchListener(this);
//        mLayoutCountdown.setOng
        mMusicLayout.setOnClickListener(this);
        mImgExpend.setOnClickListener(this);
        mBtnSpeak.setOnClickListener(this);
        mBtnSpeak.setAudioFinishRecorderListener(this);

        mDetector = new GestureDetector(this);
    }


    public void initData() {
//        mPlayerManager = HiMediaPlayerManager.getMediaPlayer(mSelf);
//        HiLog.d(TAG, mPlayerManager.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        initMusic();
    }

    private void initMusic() {
//        mMusicView.setCoverURL("https://upload.wikimedia.org/wikipedia/en/b/b3/MichaelsNumberOnes.JPG");
        if (mPlayerManager == null)
            mPlayerManager = HiMediaPlayerManager.getMediaPlayer(mSelf);
        HiLog.d(TAG, mPlayerManager.toString());
        HiLog.d(TAG, "isPlaying: " + mPlayerManager.isPlaying());
        HiLog.d(TAG, "isPaused: " + mPlayerManager.isPaused());
        if (mPlayerManager.isPlaying()) {
            mMusicView.setMax(mPlayerManager.getMaxProgress());
            mMusicView.setProgress(mPlayerManager.getProgress());
            mMusicView.start();
            mPlayerManager.setOnPlayingListener(new OnPlayingListener() {
                @Override
                public void onProgress(int progress) {
//                    mMusicView.setProgress(mPlayerManager.getProgress());
                }

                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!mMusicView.isRotating()) {
                        mMusicView.stop();
                    }
                }
            });
        } else if (mPlayerManager.isPaused()) {
            //paused
            mMusicView.setMax(mPlayerManager.getMaxProgress());
            mMusicView.setProgress(mPlayerManager.getProgress());
            mMusicView.stop();
        }
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

            case R.id.music_layout:
                //场景动画2  多个view 分别绑定对应的 transitionName
                Intent intent4 = new Intent(mSelf, HiMusicPlayerActivity.class);
                Pair<View, String> music_layout = Pair.create(((View) mMusicLayout), getString(R.string.transition));
                Pair<View, String> music_view = Pair.create(((View) mMusicView), getString(R.string.transition1));

                ActivityOptionsCompat optionsCompat4 = ActivityOptionsCompat.makeSceneTransitionAnimation(mSelf, music_layout, music_view);
                ActivityCompat.startActivity(mSelf, intent4, optionsCompat4.toBundle());
                break;

            case R.id.music_view:
                if (mPlayerManager.isPaused()) {
                    resumeMusic();
                } else {
                    playMusic();
                }
                break;
            default:
                break;
        }
    }

    private void resumeMusic() {
        mPlayerManager.resume();
        mMusicView.start();
    }


    private void playMusic() {
        if (!mMusicView.isRotating()) {
            mPlayerManager.playSound(null, R.raw.jaychou1, new OnPlayingListener() {
                @Override
                public void onProgress(int progress) {
//                    mMusicView.setProgress(progress);
                }

                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!mMusicView.isRotating()) {
                        mMusicView.stop();
                    }
                }
            });
            mMusicView.setMax(mPlayerManager.getMaxProgress());
            mMusicView.start();
        } else {
            mPlayerManager.pause();
            mMusicView.stop();
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
                        mPlayerManager.playSound(filePath, 0, new OnPlayingListener() {
                            @Override
                            public void onProgress(int progress) {

                            }

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                builder.show();
                            }
                        });
//                        try {
//                            dialog.wait();
//
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File audio = new File(filePath);
                        if (audio.exists()) {
                            HiUploadAudioUtil.uploadVoice("http://192.168.2.101:8080/WananBackend/voiceupload", audio, new OnResponseListener() {
                                @Override
                                public void onSuccess(Object result) {
//                                    HiToast.showToast(mSelf, result.toString());
                                    Log.d(TAG, result.toString());
                                }

                                @Override
                                public void onFailure(int errorCode, String error) {
//                                    HiToast.showToast(mSelf, errorCode + error);
                                    Log.d(TAG, errorCode + error);
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


    /**
     *
     * @param e
     * @return
     */

    private static final int FLING_MIN_DISTANCE = 50;
    private static final int FLING_MIN_VELOCITY = 0;

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // TODO Auto-generated method stub
        if (e1.getX()-e2.getX() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // Fling left
            Toast.makeText(mSelf, "向左手势", Toast.LENGTH_SHORT).show();
        } else if (e2.getX()-e1.getX() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // Fling right
            Toast.makeText(mSelf, "向右手势", Toast.LENGTH_SHORT).show();
        }
        if (e1.getY()-e2.getY() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // Fling left
            Toast.makeText(mSelf, "向上手势", Toast.LENGTH_SHORT).show();
        } else if (e2.getY()-e1.getY() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // Fling right
            Toast.makeText(mSelf, "向下手势", Toast.LENGTH_SHORT).show();
        }
        return false;
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }
}
