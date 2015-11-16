package com.hdu.team.hiwanan.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RadioButton;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.activity.HiWanAnActivity;
import com.hdu.team.hiwanan.activity.HiWanAnShareActivity;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiGoodNightFragment extends Fragment implements View.OnClickListener {

    /**Values*/
    private static final String TAG = "HiGoodNightFragment";

    /**Constants*/
    private View mContentView;
    private Activity mSelf;

    /**Views*/
    private RadioButton mbtnHiWanAn;
    private RadioButton mbtnCollection;
    private RadioButton mbtnHelpSleep;

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
            mSelf = getActivity();
            mContentView = inflater.inflate(R.layout.layout_good_night, null);
            setupViews();
        }
        return mContentView;
    }

    private void setupViews() {
        mbtnHiWanAn = (RadioButton) mContentView.findViewById(R.id.btn_hi_wanan);
        mbtnCollection = (RadioButton) mContentView.findViewById(R.id.btn_collection_wanan);
        mbtnHelpSleep = (RadioButton) mContentView.findViewById(R.id.btn_help_sleep);
        mbtnHiWanAn.setOnClickListener(this);
        mbtnCollection.setOnClickListener(this);
        mbtnHelpSleep.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_hi_wanan:
                HiStartActivity(mSelf, HiWanAnActivity.class);
                break;

            case R.id.btn_collection_wanan:
                HiStartActivity(mSelf, HiWanAnShareActivity.class);
                break;

            case R.id.btn_help_sleep:

                break;

            default:
                break;
        }
    }

    /**无参数的Activity跳转*/
    public void HiStartActivity(Activity a, Class toActivity){
        Intent intent = new Intent(a, toActivity);
        startActivity(intent);
    }
}
