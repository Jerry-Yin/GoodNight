package com.hdu.team.hiwanan.manager;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by JerryYin on 11/10/15.
 * 用于MediaPlayer播放音频的管理类
 * 根据 官网 http://developer.android.com/intl/zh-cn/reference/android/media/MediaPlayer.html
 * 的MediaPlayer 流程方法编写状态图
 */
public class HiMediaPlayerManager {

    private static MediaPlayer mMediaPlayer;
    private static boolean isPaused;    //是否暂停掉

    /** 播放音频*/
    public static void playSound(String filePath,
                                 MediaPlayer.OnCompletionListener onCompletionListener) {
        if (mMediaPlayer == null){
            mMediaPlayer =  new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        }else {
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);

            // 此处播放音频出现过一个问题，具体参考： http://blog.csdn.net/zbcll2012/article/details/44020931

            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            mMediaPlayer.setDataSource(fis.getFD());

//            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
//            mMediaPlayer.prepareAsync();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 暂停*/
    public static void pause(){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            isPaused = true;
        }
    }

    /** 恢复*/
    public static void resume(){
        if (mMediaPlayer != null && isPaused){
            mMediaPlayer.start();
            isPaused = false;
        }
    }

    /** activity销毁时释放资源*/
    public static void release() {
        if (mMediaPlayer != null ){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

    }

    }
