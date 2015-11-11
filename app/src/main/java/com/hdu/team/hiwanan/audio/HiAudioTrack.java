package com.hdu.team.hiwanan.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by JerryYin on 11/11/15.
 * 接受和播放音频
 */
public class HiAudioTrack extends Thread {


    /*根据音频数据的特性来确定所要分配缓冲区的最小size*/
    private int bufSize;
    private AudioTrack mAudioTrack;
//    private byte[] bytes_pkg;

    protected byte[] mOutBytes;
    protected boolean mkeepRunning;
    private Socket mSocket;
    private DataInputStream mDataInputStream;


//    public void main(){
//        mAudioTrack.play();
//
//        mAudioTrack.write(bytes_pkg, 0, bytes_pkg.length);
//
//        mAudioTrack.stop();
//        mAudioTrack.release();
//
//    }

    public void initValues() {
        bufSize = AudioTrack.getMinBufferSize(
                8000,
//            AudioFormat.CHANNEL_CONFIGURATION_STEREO,     //双声道
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        mAudioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                8000,
//            AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufSize,
                AudioTrack.MODE_STREAM);
        try {
            mSocket = new Socket("192.168.1.100", 4331);
            mDataInputStream = new DataInputStream(mSocket.getInputStream());
            mkeepRunning = true;
            mOutBytes = new byte[bufSize];
            // new Thread(R1).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        {
            byte[] bytes_pkg = null;
            mAudioTrack.play();
            while (mkeepRunning) {
                try {
                    mDataInputStream.read(mOutBytes);
                    bytes_pkg = mOutBytes.clone();
                    mAudioTrack.write(bytes_pkg, 0, bytes_pkg.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            mAudioTrack.stop();
            mAudioTrack = null;
            try {
                mDataInputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void free() {
        mkeepRunning = false;
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.d("sleep exceptions...\n", "");
        }
    }
}
