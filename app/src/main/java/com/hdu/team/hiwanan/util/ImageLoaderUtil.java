package com.hdu.team.hiwanan.util;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * Created by JerryYin on 7/14/16.
 * 基于universalimageloader 的图片加载
 */
public class ImageLoaderUtil {

    public static ImageLoader mImageLoader = ImageLoader.getInstance();

    /**
     * 加载网络图片
     * @param imgUrl
     * @param imageView
     */
    public static void displayWebImage(String imgUrl, ImageView imageView){
        mImageLoader.displayImage(imgUrl, new ImageViewAware(imageView));
    }


    /**
     * 加载本地文件图片
     * @param imgUrl
     * @param imageView
     */
    public static void displayFileImage(String imgUrl, ImageView imageView){
        mImageLoader.displayImage("file://" + imgUrl, new ImageViewAware(imageView));
    }

    /**
     * 加载本地Drawable文件夹下的图片
     * @param uri
     * @param imageView
     */
    public static void displayDrawableImage(String uri, ImageView imageView) {
        mImageLoader.displayImage("drawable://" + uri, new ImageViewAware(imageView));
    }
}
