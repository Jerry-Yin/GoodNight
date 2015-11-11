package com.hdu.team.hiwanan.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Created by JerryYin on 11/11/15.
 * 录音
 */
public class HiAudioRecord extends Thread {

    private int mMinBufSize;
    private AudioRecord mAudioRecord;

    private byte[] mInBytes;
    private Socket mSocket;
    private boolean mKeepRunning;
    private LinkedList<byte[]> mInLists;
    private DataOutputStream mOutputStream;


    @Override
    public void run() {
        try {
            byte[] bytePkg;
            mAudioRecord.startRecording();
            while (mKeepRunning) {
                mAudioRecord.read(mInBytes, 0, mMinBufSize);
                bytePkg = mInBytes.clone();
                if (mInLists.size() >= 2) {
                    mOutputStream.write(mInLists.removeFirst(), 0, mInLists.removeFirst().length);
                }
                mInLists.add(bytePkg);
            }
            mAudioRecord.stop();
            mAudioRecord = null;
            mInBytes = null;
            mOutputStream.close();

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initValues() {
        mMinBufSize = AudioRecord.getMinBufferSize(8000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                8000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                mMinBufSize);

        mInBytes = new byte[mMinBufSize];
        mKeepRunning = true;
        mInLists = new LinkedList<>();

        try {
            mSocket = new Socket("192.168.1.100", 4332);
            mOutputStream = new DataOutputStream(mSocket.getOutputStream());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void free() {
        mKeepRunning = false;
        try {
            Thread.sleep(1000);


        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("sleep exceptions...\n", "");
        }
    }
}
