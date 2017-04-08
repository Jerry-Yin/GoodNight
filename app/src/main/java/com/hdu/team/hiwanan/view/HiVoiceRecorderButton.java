package com.hdu.team.hiwanan.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.manager.HiMediaRecordManager;
import com.hdu.team.hiwanan.manager.HiVoiceRecDialogManager;
import com.hdu.team.hiwanan.util.common.HiTimesUtil;


/**
 * Created by JerryYin on 11/9/15.
 * 发送语音消息的按钮button类
 */
public class HiVoiceRecorderButton extends Button implements HiMediaRecordManager.AudioStateListener {

    /** 三种状态*/
    private final static int STATE_NORMAL = 1;
    private final static int STATE_SPEARKING = 2;
    private final static int STATE_WANT_TO_CANCEL = 3;
    /** 当前状态 （默认正常）*/
    private int mCurState = STATE_NORMAL;
    /** y方向超出一定距离 认为取消录音*/
    private final static int DISTANCE_Y_CANCEL = 50;
    /** 是否已经开始录音*/
    private boolean isRecording = false;
    /** 需要整合的dialog*/
    private HiVoiceRecDialogManager mDialogManager;
    private HiMediaRecordManager mHiMediaRecordManager;

    private float mTime;    //计时时长
    private boolean mReady;    //是否触发LongClick；

    /** 振动器*/
    private Vibrator mVibrator;


    public HiVoiceRecorderButton(Context context) {
        this(context, null);
    }

    public HiVoiceRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**整合dialog到button中*/
        initDialog(context);

        /**
         * 整合音频部分
         * 音频存储在外置sd卡根目录的 /hiwanan_voice_audios/ 文件夹下
         */
        initMediaRecorder();

    }

    private void initDialog(Context context) {
        mDialogManager = new HiVoiceRecDialogManager(getContext());
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mReady = true;
                mHiMediaRecordManager.prepareAudio(HiTimesUtil.getCurDateTime());
                return false;
            }
        });
    }

    private void initMediaRecorder() {
//        String dir = Environment.getExternalStorageDirectory() + "/hiwanan_voice_audios";
        mHiMediaRecordManager = HiMediaRecordManager.getInstance(HiConfig.APP_VOICE_DIR);
        mHiMediaRecordManager.setOnAudioStateListener(this);        //录音准备完毕回调
    }

    /**
     * 录音完成后的回调接口
     */
    public interface OnFinishRecorderListener {
        void onFinish(float times, String filePath);
    }

    private OnFinishRecorderListener mListener;

    public void setAudioFinishRecorderListener(OnFinishRecorderListener listener) {
        mListener = listener;
    }

    //录音准备完毕
    @Override
    public void OnDenePrepared() {
        mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    /**
     * 获取音量大小的runnable
     * 每隔0.1s获取
     * 还可以用来计时 ，录音时长
     */
    private Runnable mGetVoiceLevelRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);  //每隔0.1s更新一次音量图片
                    mTime += 0.1f;      //累计时长
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * handler,用来协同线程，完成振动，dialog显示， 更新音量图片，累计录音时长 以及 关闭dialog；
     */
    private static final int MSG_AUDIO_PREPARED = 0X001;
    private static final int MSG_VOICE_CHANGED = 0X002;
    private static final int MSG_DIALOG_DISMISS = 0X003;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    //显示应该是在 audio end prepared 以后
                    mDialogManager.showRecordingDialog();
                    showVibrator();
                    isRecording = true;
                    //更新音量，需要开启一个线程
                    new Thread(mGetVoiceLevelRunnable).start();

                    break;
                case MSG_VOICE_CHANGED:
                    mDialogManager.updateVoiceLevel(mHiMediaRecordManager.getVoiceLevel(7));
                    break;
                case MSG_DIALOG_DISMISS:
                    mDialogManager.dismissDialog();
                    break;
            }
        }

        ;
    };

    //振动
    private void showVibrator() {
        //震动一秒
        mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (mVibrator.hasVibrator()) {
            //条件是设备有振动器
            long[] param = {0, 10};     //(等待开启震动的时间， 持续时间)
            mVibrator.vibrate(param, -1);    //(时间,  －1 标识只震动一次)
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //当前的x，y坐标
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //TODO
//                isRecording = true;
                changeState(STATE_SPEARKING);
                break;

            case MotionEvent.ACTION_MOVE:
                if (isRecording) {
                    //根据x，y坐标判断是否想要取消
                    if (wantToCancel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_SPEARKING);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (!mReady) {
                    //还没有触发LongClick
                    reset();
                    return super.onTouchEvent(event);
                }
                if (!isRecording || mTime < 0.6f) {
                    //prepar还未完成就up
                    mDialogManager.tooShort();
                    mHiMediaRecordManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1300);     //延迟，1.3s以后关闭dialog
                } else if (mCurState == STATE_SPEARKING) {
                    //正常录制结束
                    mDialogManager.dismissDialog();
                    mHiMediaRecordManager.release();

                    if (mListener != null) {
                        mListener.onFinish(mTime, mHiMediaRecordManager.getCurFilePath());
                    }
                    //todo callbackToActivity保存录音

                } else if (mCurState == STATE_WANT_TO_CANCEL) {
                    mDialogManager.dismissDialog();
                    mHiMediaRecordManager.cancel();
                    //cancel()

                }
                reset();
                break;
        }

        return super.onTouchEvent(event);
    }

    //恢复所有标志位
    private void reset() {
        isRecording = false;
        mReady = false;
        changeState(STATE_NORMAL);
        mTime = 0;
    }

    /**
     * 根据x，y坐标判断是否想要取消
     * x : 超出按钮坐标之外
     * y : 超出上下一定距离
     */
    private boolean wantToCancel(int x, int y) {
        if (x < 0 || x > getWidth()) {
            return true;
        }
        if (y < -DISTANCE_Y_CANCEL) {
            return true;
        }
        return false;
    }

    //改变当前按钮的状态
    private void changeState(int state) {
        if (mCurState != state) {
            mCurState = state;
            switch (state) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_speak_normal);
                    setText(R.string.str_press_to_speak_normal);

                    break;

                case STATE_SPEARKING:
                    setBackgroundResource(R.drawable.btn_speak_recording);
                    setText(R.string.str_press_to_speak_recording);
                    if (isRecording) {
                        //todo Dialog.recording();
                        mDialogManager.recording();

                    }
                    break;

                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.btn_speak_recording);
                    setText(R.string.str_press_to_speak_cancel);
                    //todo Dialog.wantToCancel();
                    mDialogManager.wantToCancel();
                    break;
            }
        }

    }


}
