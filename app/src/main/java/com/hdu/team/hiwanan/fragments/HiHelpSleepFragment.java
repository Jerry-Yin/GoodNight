package com.hdu.team.hiwanan.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.base.HiBaseFragment;

/**
 * Created by JerryYin on 9/21/16.
 */
public class HiHelpSleepFragment extends HiBaseFragment {


    /**
     * Constants
     */
    private View mContentView ;
    private Activity mSelf;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != mContentView) {
            ViewGroup vg = (ViewGroup) mContentView.getParent();
            if (null != vg) {
                vg.removeView(mContentView);
            }
        } else {
            mSelf = getActivity();
            mContentView = inflater.inflate(R.layout.layout_help_sleep, null);
            initViews();
            initData();
        }
        return mContentView;
    }

    public void initViews() {

    }

    public void initData() {
        super.initData();
    }


}
