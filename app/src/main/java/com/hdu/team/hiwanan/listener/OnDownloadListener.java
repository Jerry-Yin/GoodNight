package com.hdu.team.hiwanan.listener;

/**
 * Created by JerryYin on 7/14/16.
 * 下载会回调接口
 */
public interface OnDownloadListener {

    /**
     *
     * @param result
     */
    void onDone(String result);

    /**
     *
     * @param code
     * @param error
     */
    void onError(int code, String error);

    /**
     *
     * @param progress 进度
     * @param speed  下载速度
     */
    void onProgress(Integer progress, long speed);

}
