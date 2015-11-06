package com.hdu.team.hiwanan.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.activity.HiAboutFunction;
import com.hdu.team.hiwanan.activity.HiVoiceSettingActivity;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiSettingFragment extends Fragment implements View.OnClickListener {


    /**Constants*/
    private View mContentView;
    private Activity mSelf;

    /**Views*/
    private RadioButton mbtnWADaRen;
    private RadioButton mbtnSettingHi;
    private RadioButton mbtnAboutApp;


    public Activity getmSelf(){
        return mSelf;
    }

    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mContentView){
            ViewGroup vg = (ViewGroup) mContentView.getParent();
            if (null != vg){
                vg.removeView(mContentView);
            }
        }else {
            mContentView = inflater.inflate(R.layout.layout_setting, null);
            mSelf = getActivity();
            setupViews();
        }
        return mContentView;
    }

    private void setupViews() {
        mbtnWADaRen = (RadioButton) mContentView.findViewById(R.id.btn_wanan_daren);
        mbtnSettingHi = (RadioButton) mContentView.findViewById(R.id.btn_setting_hi_voice);
        mbtnAboutApp = (RadioButton) mContentView.findViewById(R.id.btn_about_function);
        mbtnWADaRen.setOnClickListener(this);
        mbtnSettingHi.setOnClickListener(this);
        mbtnAboutApp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_wanan_daren:

                break;

            case R.id.btn_setting_hi_voice:
                Intent intent = new Intent(mSelf, HiVoiceSettingActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_about_function:
                Intent intent2 = new Intent(mSelf, HiAboutFunction.class);
                startActivity(intent2);
                break;

            default:
                break;
        }
    }
}
