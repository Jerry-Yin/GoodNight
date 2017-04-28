package com.hdu.team.hiwanan.manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.hdu.team.hiwanan.activity.TestActivity;
import com.hdu.team.hiwanan.listener.OnPlayingListener;
import com.hdu.team.hiwanan.util.HiLog;
import com.hdu.team.hiwanan.util.HiToast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by JerryYin on 11/10/15.
 * 用于MediaPlayer播放音频的管理类
 * 根据 官网 http://developer.android.com/intl/zh-cn/reference/android/media/MediaPlayer.html
 * 的MediaPlayer 流程方法编写状态图
 */
public class HiMediaPlayerManager {

    private static final java.lang.String TAG = "HiMediaPlayerManager";

    private Context mContext;
    private static HiMediaPlayerManager mInstance;
    private MediaPlayer mMediaPlayer;
    private boolean isPaused;    //是否暂停掉
    private String mFilePath;
    private Timer mTimer;
//    private OnPlayingListener mPlayingListener;


    private HiMediaPlayerManager(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
//        if (mMediaPlayer == null) {
//            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//                @Override
//                public boolean onError(MediaPlayer mp, int what, int extra) {
//                    mMediaPlayer.reset();
//                    return false;
//                }
//            });
//        }
//        setDataSource();

        if (mTimer == null)
            mTimer = new Timer();
    }


    public static HiMediaPlayerManager getMediaPlayer(Context context) {
        synchronized (HiMediaPlayerManager.class) {
            if (mInstance == null) {
                mInstance = new HiMediaPlayerManager(context);
            }
        }
        return mInstance;
    }

    /**
     * 播放音频
     *
     * @param filePath 从文件链接播放
     * @param musicId  内置音乐播放 (R.raw.voice)
     * @param listener 可以拿到播放进度以及
     */
    public void playSound(String filePath, int musicId, OnPlayingListener listener) {
        if (mMediaPlayer != null)
            mMediaPlayer.reset();

        if (filePath != null) {
            try {
                mMediaPlayer = new MediaPlayer();
                File file = new File(filePath);
                HiLog.d(TAG, "filePath:" + filePath);
                HiLog.d(TAG, "is file :" + file.isFile());
                FileInputStream fis = new FileInputStream(file);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(fis.getFD());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (musicId != 0) {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
            mMediaPlayer = MediaPlayer.create(mContext, musicId);
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mMediaPlayer.reset();
                return false;
            }
        });


        try {
            mMediaPlayer.prepare();
            //            mMediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e){
            e.printStackTrace();
            HiLog.e(TAG, "IllegalStateException "+e.getMessage());
        }

        mMediaPlayer.start();
        HiLog.d(TAG, "start playing...");

        if (listener != null)
            this.setOnPlayingListener(listener);
        // 此处播放音频出现过一个问题，具体参考： http://blog.csdn.net/zbcll2012/article/details/44020931

    }

    /**
     * 播放时的监听，可以单独使用方法，也可以直接播放时使用
     *
     * @param listener
     */
    public void setOnPlayingListener(final OnPlayingListener listener) {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            if (listener != null) {
                mMediaPlayer.setOnCompletionListener(listener);
                if (mTimer != null) {
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            listener.onProgress(mMediaPlayer.getCurrentPosition() / 1000);
                            HiLog.d(TAG, "cur: " + mMediaPlayer.getCurrentPosition() / 1000);
                        }
                    }, 0, 100);
                }
            }
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            isPaused = true;
        }
    }

    /**
     * 恢复
     */
    public void resume() {
        if (mMediaPlayer != null && isPaused) {
            mMediaPlayer.start();
            isPaused = false;
        }
    }

    /**
     * activity销毁时释放资源
     */
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    public int getProgress() {
        int progress = 0;
        if (mMediaPlayer != null) {
            progress = mMediaPlayer.getCurrentPosition() / 1000;
        }
        return progress;
    }

    /**
     * minute =  (duration / 1000) / 60
     * second =  (duration / 1000) % 60
     *
     * @return
     */
    public int getMaxProgress() {
        int max = 0;
        if (mMediaPlayer != null) {
            max = mMediaPlayer.getDuration() / 1000;
        }
        return max;
    }

    /**
     * is paused ?
     *
     * @return
     */
    public boolean isPaused() {
        return this.isPaused;
    }

    public boolean isPlaying(){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
            return true;
        else
            return false;
    }
}
