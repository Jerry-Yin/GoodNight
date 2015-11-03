package com.hdu.team.hiwanan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.fragments.HiGoodNightFragment;
import com.hdu.team.hiwanan.fragments.HiHomePageFragment;
import com.hdu.team.hiwanan.fragments.HiSettingFragment;
import com.hdu.team.hiwanan.fragments.HiSleepFragment;
import com.hdu.team.hiwanan.util.HiToast;


public class HiMainActivity extends HiActivity implements View.OnClickListener {

    private static final String TAG = "HiMainActivity";
    public static HiActivity mIntance = null;

    /**Views*/
    private RadioButton mbtnHomePage;
    private RadioButton mbtnGoodNight;
    private RadioButton mbtnSleep;
    private RadioButton mbtnSetting;

    private TextView mtextTitle;

    /**Fragments*/
    private FragmentManager mFragmentManager;
    private HiHomePageFragment mHomePageFragment;
    private HiGoodNightFragment mGoodNightFragment;
    private HiSleepFragment mSleepFragment;
    private HiSettingFragment mSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (LogoActivity.isConnect(this) == false) {
//            Dialog netDialog = new AlertDialog.Builder(this).setTitle("提示")
//                    .setMessage("联网失败，请检查手机网络状态").setPositiveButton("确定", null)
//                    .create();
//            netDialog.show();
//            Toast.makeText(this, "联网失败，请检查手机网络状态", Toast.LENGTH_LONG).show();
//        HiToast.showToast(mIntance, "联网失败，请检查手机网络状态");
//        }
        setContentView(R.layout.activity_hi_main);
        mIntance = HiMainActivity.this;

        initViews();

    }

    public void initViews() {
        mFragmentManager = getFragmentManager();
        mtextTitle = (TextView) findViewById(R.id.text_title);
        mbtnHomePage = (RadioButton) findViewById(R.id.btn_home_page);
        mbtnGoodNight = (RadioButton) findViewById(R.id.btn_good_night);
        mbtnSleep = (RadioButton) findViewById(R.id.btn_sleep);
        mbtnSetting = (RadioButton) findViewById(R.id.btn_setting);
        mbtnHomePage.setOnClickListener(this);
        mbtnGoodNight.setOnClickListener(this);
        mbtnSleep.setOnClickListener(this);
        mbtnSetting.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_home_page:
                changeFragment(getHomeFragment(), false);
                mtextTitle.setText(R.string.home_page);
                break;

            case R.id.btn_good_night:
                changeFragment(getGoodNightFragment(), false);
                mtextTitle.setText(R.string.good_night);
                break;

            case R.id.btn_sleep:
                changeFragment(getSleepFragment(), false);
                mtextTitle.setText(R.string.sleep);
                break;

            case R.id.btn_setting:
                changeFragment(getSettingFragment(), false);
                mtextTitle.setText(R.string.setting);
                break;

            default:
                break;
        }
    }


    /**获取fragment实例*/
    public HiHomePageFragment getHomeFragment(){
        if (mHomePageFragment == null){
            mHomePageFragment = new HiHomePageFragment();
        }
        return mHomePageFragment;
    }

    public HiGoodNightFragment getGoodNightFragment(){
        if (mGoodNightFragment == null){
            mGoodNightFragment = new HiGoodNightFragment();
        }
        return mGoodNightFragment;
    }

    public HiSleepFragment getSleepFragment(){
        if (mSleepFragment == null){
            mSleepFragment = new HiSleepFragment();
        }
        return mSleepFragment;
    }

    public HiSettingFragment getSettingFragment(){
        if (mSettingFragment == null){
            mSettingFragment = new HiSettingFragment();
        }
        return mSettingFragment;
    }

    /**替换fragment*/
    public void changeFragment(Fragment f, boolean init){
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.frgment_root, f);
        if (!init){
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

}
