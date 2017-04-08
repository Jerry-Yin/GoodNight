package com.hdu.team.hiwanan.listener;

/**
 * Created by JerryYin on 7/13/16.
 */
public interface OnResponseListener<T> {

    void onSuccess(T result);

    void onFailure(int errorCode, String error);

}