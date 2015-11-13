package com.hdu.team.hiwanan.network;

import java.util.Objects;

/**
 * Created by JerryYin on 11/12/15.
 */
public interface OnResponseListener {

    /** ServerTalker will callback this method after connect with server.
     * @param response response the data send by server.
     * @param actionId taskId taskId is the check code.
     * @param rawData
     */
    public void onResponse(Object response, String actionId, boolean rawData);

    public boolean isDisable();
}
