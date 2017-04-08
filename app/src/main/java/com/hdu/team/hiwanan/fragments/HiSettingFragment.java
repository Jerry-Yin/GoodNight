package com.hdu.team.hiwanan.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.activity.HiAboutFunction;
import com.hdu.team.hiwanan.activity.HiLoginActivity;
import com.hdu.team.hiwanan.activity.HiMainActivity;
import com.hdu.team.hiwanan.activity.HiUserInfoActivity;
import com.hdu.team.hiwanan.activity.HiVoiceSettingActivity;
import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.model.bmob.UserBmob;
import com.hdu.team.hiwanan.util.HiSharedPreferenceUtil;
import com.hdu.team.hiwanan.util.ImageLoaderUtil;

import cn.bmob.v3.BmobUser;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiSettingFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "HiSettingFragment";
    /**
     * Constants
     */
    private View mContentView;
    private Activity mSelf;

    /**
     * Views
     */
    private LinearLayout mbtnPersonal;
    private RadioButton mbtnWADaRen;
    private RadioButton mbtnSettingHi;
    private RadioButton mbtnAboutApp;
    private RadioButton mbtnLogout;
    private TextView mTxtName;
    private ImageView mImgIcon;

    private AlertDialog.Builder mAlertDialog;



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
            mContentView = inflater.inflate(R.layout.layout_setting, null);
            initViews();
            initData();
        }
        return mContentView;
    }

    public void initViews() {
        mbtnPersonal = (LinearLayout) mContentView.findViewById(R.id.btn_personal);
        mTxtName = (TextView) mContentView.findViewById(R.id.text_account_name);
        mbtnWADaRen = (RadioButton) mContentView.findViewById(R.id.btn_wanan_daren);
        mbtnSettingHi = (RadioButton) mContentView.findViewById(R.id.btn_setting_hi_voice);
        mbtnAboutApp = (RadioButton) mContentView.findViewById(R.id.btn_about_function);
        mbtnLogout = (RadioButton) mContentView.findViewById(R.id.btn_logout);
        mImgIcon = (ImageView) mContentView.findViewById(R.id.img_account);
        mbtnPersonal.setOnClickListener(this);
        mbtnWADaRen.setOnClickListener(this);
        mbtnSettingHi.setOnClickListener(this);
        mbtnAboutApp.setOnClickListener(this);
        mbtnLogout.setOnClickListener(this);
        mImgIcon.setOnClickListener(this);
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(mSelf);
        }
    }

    public void initData(){

    }

    /**
     * 加载用户信息
     */
    private void loadUsrInfo() {
        UserBmob usr = BmobUser.getCurrentUser(UserBmob.class);
        Log.d(TAG, "curUsr = " + usr);
        Log.d(TAG, "name = " + usr.getUsername());
        Log.d(TAG, "icon = " + usr.getIcon());    //用户头像url
        Log.d(TAG, "Environment.getExternalStorageDirectory() = " + Environment.getExternalStorageDirectory());  // =/storage/emulated/0
        Log.d(TAG, "context.getApplicationContext().getCacheDir() = " + mSelf.getApplicationContext().getCacheDir());// = /data/data/com.hdu.team.hiwanan/cache

//        BmobNetworkUtils.downloadIcon(mSelf, new BmobFile("icon.png", null, usr.getIcon()), new OnDownloadListener() {
//            Message message = new Message();
//            @Override
//            public void onDone(String result) {
//                Log.d(TAG, "icon = "+result);
//                message.what = HiRequestCodes.DOWNLOAD_SUCCESS;
//                message.obj = result;
//
//            }
//
//            @Override
//            public void onError(int code, String error) {
//                Log.d(TAG, "error = "+code+ " "+error);
//
//            }
//
//            @Override
//            public void onProgress(Integer progress, long speed) {
//
//            }
//        });

        ImageLoaderUtil.displayWebImage(usr.getIcon(), mImgIcon);   // 加载用户头像
        mTxtName.setText(usr.getUsername());

    }


    @Override
    public void onResume() {
        super.onResume();
        loadUsrInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_personal:
                Intent i = new Intent(mSelf, HiUserInfoActivity.class);
                startActivity(i);
                // TODO: 7/16/16 个人信息页面修改了一些信息后，切换回来的时候需要更新


                break;

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

            case R.id.btn_logout:
                showDialog();
                break;

            case R.id.img_account:

                break;

            default:
                break;
        }
    }

    private void showDialog() {
        mAlertDialog.setMessage("退出当前账号！")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 7/15/16 清除本地sharePref用户信息 ＋ 退出应用
                        clearLocalUsr();
                        Intent intent = new Intent(mSelf, HiLoginActivity.class);
                        startActivity(intent);
                        HiMainActivity.mIntance.finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAlertDialog.create().dismiss();
                    }
                }).create().show();
    }

    /**
     * 清除本地用户信息
     */
    private void clearLocalUsr() {
        HiSharedPreferenceUtil.setDataToNull(mSelf, HiConfig.HI_PREFERENCE_NAME, HiConfig.KEY_USER_NAME);
        HiSharedPreferenceUtil.setDataToNull(mSelf, HiConfig.HI_PREFERENCE_NAME, HiConfig.KEY_PASSWORD);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
}
