package com.hdu.team.hiwanan.view;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by JerryYin on 11/10/15.
 * 单例化 编写
 */
public class HiAudioManager {

    private MediaRecorder mediaRecorder;
    private String mDir;    //文件夹，用于保存录音文件
    private String mCurFilePath;    //文件夹路径     (需要回传) -->Button -->Activity

    private boolean isPrepared = false;

    /**
     * 单例化
     */
    private static HiAudioManager mInstance;

    private HiAudioManager(String dir) {
        mDir = dir;
    }

    public String getCurFilePath() {
        return mCurFilePath;
    }

    /**
     * 回调准备完毕
     */
    public interface AudioStateListener {
        void donePrepared();
    }

    public AudioStateListener mAudioStateListener;

    public void setOnAudioStateListener(AudioStateListener listener) {
        mAudioStateListener = listener;
    }

    /**
     * 工厂方法
     */
    public static HiAudioManager getInstance(String dir) {
        if (mInstance == null) {
            synchronized (HiAudioManager.class) {        // 同步
                if (mInstance == null) {
                    mInstance = new HiAudioManager(dir);
                }
            }
        }
        return mInstance;
    }


    public void prepareAudio() {
        //创建文件夹
        try {
            isPrepared = false;

            File dir = new File(mDir);
            if (!dir.exists())
                dir.mkdirs();

            //根据文件夹创建文件
            String fileName = generateFileName();
            File file = new File(dir, fileName);

            mCurFilePath = file.getAbsolutePath();

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setOutputFile(file.getAbsolutePath());       //设置输出文件
            /** 此处需根据 官网 http://developer.android.com/intl/zh-cn/reference/android/media/MediaRecorder.html
             *  的状态图严格执行各个流程方法
             */
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);        // 音频源－－麦克风
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);  // 输出格式 (api<10 ? RAW_AMR : AMR_NB )
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);   // 编码
            mediaRecorder.prepare();
            mediaRecorder.start();

            //准备阶段 结束
            isPrepared = true;
            //TODO 可以开始录音
            if (mAudioStateListener != null){
                mAudioStateListener.donePrepared();
            }

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 随即生成文件的名称
     * 音频的后缀名为 .amr
     */
    private String generateFileName() {
        return UUID.randomUUID().toString() + "amr";
    }

    /**
     * maxLevel = 7;
     * mediaRecorder.getMaxAmplitude() 为 0-－32767
     * 需要返回的数 1-－7
     */
    public int getVoiceLevel(int maxLevle) {
        try {
            if(isPrepared){
                return maxLevle * mediaRecorder.getMaxAmplitude() / 32768 + 1;
            }
        } catch (Exception e) {

        }
        return 1;
    }

    public void release() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    public void cancel() {
        release();
        //删除文件
        if (mCurFilePath != null){
            File file = new File(mCurFilePath);
            file.delete();
            mCurFilePath = null;
        }
    }
}
