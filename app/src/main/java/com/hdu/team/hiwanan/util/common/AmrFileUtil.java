package com.hdu.team.hiwanan.util.common;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by JerryYin on 7/19/16.
 * .amr 格式录音文件 工具类
 */
public class AmrFileUtil {


    /**
     * 得到amr的时长
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static long getAmrDuration(File file) throws IOException {
        long duration = -1;
        int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0 };
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            long length = file.length();//文件的长度
            int pos = 6;//设置初始位置
            int frameCount = 0;//初始帧数
            int packedPos = -1;
            /////////////////////////////////////////////////////
            byte[] datas = new byte[1];//初始数据值
            while (pos <= length) {
                randomAccessFile.seek(pos);
                if (randomAccessFile.read(datas, 0, 1) != 1) {
                    duration = length > 0 ? ((length - 6) / 650) : 0;
                    break;
                }
                packedPos = (datas[0] >> 3) & 0x0F;
                pos += packedSize[packedPos] + 1;
                frameCount++;
            }
            /////////////////////////////////////////////////////
            duration += frameCount * 20;//帧数*20
        } finally {
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        }
        return duration  / 1000;
    }

    /**
     * 不行，，，
     * @param context
     * @param file
     * @return
     */
    public static long getAmrDuration(Context context, File file){
        MediaPlayer player = new MediaPlayer();
        long d = 0;
        try {
            player.setDataSource(context, Uri.fromFile(file));
//                player.prepare();
//            Log.d(TAG, "d = "+ player.getDuration());
            d = player.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }


}
