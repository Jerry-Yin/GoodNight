package com.hdu.team.hiwanan.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;

/**
 * Created by JerryYin on 11/9/15.
 */
public class HiVoiceRecDialogManager {

    /**
     * Values
     */
    private Context mContext;
    private Dialog mDialog;

    /**
     * Views
     */
    private ImageView mImgRecIcon;
    private ImageView mImgRecVoice;
    private TextView mtxtRecDialog;


    public  HiVoiceRecDialogManager(Context context) {
        mContext = context;
    }

    /**
     * 需要提供一下几个方法
     */
    public void showRecordingDialog() {
        mDialog = new Dialog(mContext, R.style.AudioDialog);     //此处为一个自定义的style
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.layout_voice_dialog, null);
        mDialog.setContentView(view);

        mImgRecIcon = (ImageView) mDialog.findViewById(R.id.img_recorder_icon);
        mImgRecVoice = (ImageView) mDialog.findViewById(R.id.img_recorder_voice);
        mtxtRecDialog = (TextView) mDialog.findViewById(R.id.text_recorder_dialog);

        mDialog.show();
    }

    public void recording() {
        if (mDialog != null && mDialog.isShowing()) {
            mImgRecIcon.setVisibility(View.VISIBLE);
            mImgRecVoice.setVisibility(View.VISIBLE);
            mtxtRecDialog.setVisibility(View.VISIBLE);

            mImgRecIcon.setImageResource(R.drawable.recorder);
//            mImgRecVoice.setImageResource(R.drawable.voice1);
            mtxtRecDialog.setText(R.string.str_recorder_dialog);
        }
    }

    public void wantToCancel() {
        if (mDialog != null && mDialog.isShowing()) {
            mImgRecIcon.setVisibility(View.VISIBLE);
            mImgRecVoice.setVisibility(View.GONE);
            mtxtRecDialog.setVisibility(View.VISIBLE);

            mImgRecIcon.setImageResource(R.drawable.voice_cancel);
            mtxtRecDialog.setText(R.string.str_press_to_speak_cancel);
        }
    }


    public void tooShort() {
        if (mDialog != null && mDialog.isShowing()) {
            mImgRecIcon.setVisibility(View.VISIBLE);
            mImgRecVoice.setVisibility(View.GONE);
            mtxtRecDialog.setVisibility(View.VISIBLE);

            mImgRecIcon.setImageResource(R.drawable.voice_to_short);
            mtxtRecDialog.setText(R.string.str_press_to_speak_tooshort);
        }
    }

    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }


    /**
     * 通过level去更新声音的级别图片效果
     * level (1--7)
     */
    public void updateVoiceLevel(int level) {
        if (mDialog != null && mDialog.isShowing()) {
//            mImgRecIcon.setVisibility(View.VISIBLE);
//            mImgRecVoice.setVisibility(View.GONE);
//            mtxtRecDialog.setVisibility(View.VISIBLE);

            int resId = mContext.getResources().getIdentifier("voice"+level, "drawable", mContext.getPackageName());

            mImgRecVoice.setImageResource(resId);
        }
    }


}
