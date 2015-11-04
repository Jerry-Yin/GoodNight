package com.hdu.team.hiwanan.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hdu.team.hiwanan.R;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiSleepFragment extends Fragment {

    /**Constants*/
    private View mContentView;
    private Activity mSelf;


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
        }else {mSelf = getActivity();
            mContentView = inflater.inflate(R.layout.layout_sleep, null);
            mSelf = getActivity();
            setupViews();
        }
        return mContentView;
    }

    private void setupViews() {


    }


}
