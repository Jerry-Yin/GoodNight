package com.hdu.team.hiwanan.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;

/**
 * Created by JerryYin on 11/12/15.
 */
public class HiLoadingDialogManager {

    /**
     * Values
     */
    private Context mContext;
    private Dialog mDialog;

    private AlertDialog.Builder builder;

    /**
     * Views
     */
    private ImageView mImgLoading;
    private TextView mTxtLoading;

    public HiLoadingDialogManager (Context context){
        this.mContext = context;
    }

    public void showLoadingDialog(){
        mDialog = new Dialog(mContext, R.style.AudioDialog);     //此处为一个自定义的style
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.layout_voice_dialog, null);
        mDialog.setContentView(view);

        mImgLoading = (ImageView) mDialog.findViewById(R.id.img_recorder_icon);
        mTxtLoading = (TextView) mDialog.findViewById(R.id.text_recorder_dialog);
        mDialog.show();

        //旧的一般的dialog
//        builder = new AlertDialog.Builder(mContext);
//
////                .setIcon(R.drawable.)
//        builder.setMessage("加载中...");
//        builder.create().show();

    }

    public void dismissDialog(){
        if (builder != null){
            builder.create().dismiss();
            builder = null;
        }
    }
}
