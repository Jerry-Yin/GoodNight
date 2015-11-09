package com.hdu.team.hiwanan.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hdu.team.hiwanan.R;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiHomePageFragment extends Fragment implements View.OnClickListener {

    /**Constants*/
    private View mContentView;
    private Activity mSelf;

    /**View*/
    private ImageButton mbtnAddClock;

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
            mContentView = inflater.inflate(R.layout.layout_home_page, container, false);
            mSelf = getmSelf();
            setupViews();
        }
        return mContentView;
    }


    private void setupViews() {
        mbtnAddClock = (ImageButton) mContentView.findViewById(R.id.btn_add_clock);
        mbtnAddClock.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_clock:

                break;

            default:
                break;
        }
    }

}
