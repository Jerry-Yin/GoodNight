package com.hdu.team.hiwanan.listener;

/**
 * Created by JerryYin on 7/16/16.
 */
public interface OnProgressListener {

    void onSuccess(Object result);

    void onFailure(int errorCode, String error);

    void onProgress(Integer progress);
}
