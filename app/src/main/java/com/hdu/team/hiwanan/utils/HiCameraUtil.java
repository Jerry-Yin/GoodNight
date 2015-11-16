package com.hdu.team.hiwanan.utils;

import android.net.Uri;
import android.os.Environment;

import com.hdu.team.hiwanan.util.HiLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JerryYin on 11/15/15.
 * 拍照（录像）工具类
 */
public class HiCameraUtil {

    public static final int MEDIA_TYPE_IMAGE = 0001;   //take photo
    private static final int MEDIA_TYPE_VIDEO = 0002;   //make video

    /** Create a file Uri for saving an image or video */
    public static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        //拍照的照片保存到本地的SD_card的 Pictures/HiWanAnCamera/ 路径下；
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "HiWanAnCamera");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                HiLog.d("HiWanAnCamera", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


}
