package com.hdu.team.hiwanan.listener;

/**
 * Created by JerryYin on 7/13/16.
 */
public interface OnResponseListener {

    void onSuccess(Object result);

    void onFailure(int errorCode, String error);


}
