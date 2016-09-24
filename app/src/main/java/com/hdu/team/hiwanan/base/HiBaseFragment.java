package com.hdu.team.hiwanan.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hdu.team.hiwanan.R;

/**
 * Created by JerryYin on 9/23/16.
 *
 * Base Fragment
 */


public class HiBaseFragment extends Fragment {


    /**
     * Constants
     */
    public static View mContentView;
    public static Activity mSelf;
    public static int mLayoutId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

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
            inflateView();
            mContentView = inflater.inflate(mLayoutId, null);
            initViews();
            initData();
        }
        return mContentView;
    }


    public static Activity getmSelf(){
        return mSelf;
    }




    /**
     * 绑定布局
     * 子类必须在这个方法里执行赋值操作(初始化layout)
     * @param
     */
    public void inflateView() {
        // TODO: 9/23/16 init layoutId
//        mLayoutId = layoutId;
    }

    public void initViews(){

    }

    public void initData(){};



}
