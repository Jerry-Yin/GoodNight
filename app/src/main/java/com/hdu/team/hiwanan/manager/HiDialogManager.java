package com.hdu.team.hiwanan.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by JerryYin on 4/7/17.
 */

public class HiDialogManager {

    private static HiDialogManager mInstance;
    private static AlertDialog.Builder mBuilder;

    private HiDialogManager(Context context) {
        mBuilder = new AlertDialog.Builder(context);
    }

    public static HiDialogManager getInstance(Context context){
        if (mInstance == null)
            mInstance = new HiDialogManager(context);
        return mInstance;
    }


    public static void showDefaultDialog(String title, String[] items, DialogInterface.OnClickListener listener,
                                         String pstvBtn, DialogInterface.OnClickListener pListener,
                                         String ngtvBtn, DialogInterface.OnClickListener nListener){
        mBuilder.setTitle(title)
                .setItems(items, listener)
                .setPositiveButton(pstvBtn, pListener)
                .setNeutralButton(ngtvBtn, nListener)
                .create().show();
    }
}
