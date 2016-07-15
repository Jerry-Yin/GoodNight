package com.hdu.team.hiwanan.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.base.HiActivity;
import com.hdu.team.hiwanan.fragments.HiGoodNightFragment;
import com.hdu.team.hiwanan.fragments.HiHomePageFragment;
import com.hdu.team.hiwanan.fragments.HiSettingFragment;
import com.hdu.team.hiwanan.fragments.HiSleepFragment;
import com.jauker.widget.BadgeView;


public class HiMainActivity extends HiActivity implements View.OnClickListener {

    /**
     * constants
     */
    private static final String TAG = "HiMainActivity";

    /**
     * Views
     */
    private RadioGroup mbuttomGroup;
    private RadioButton mbtnHomePage;
    private RadioButton mbtnGoodNight;
    private RadioButton mbtnSleep;
    private RadioButton mbtnSetting;

    private TextView mtextTitle;
    private RelativeLayout mTitleBar;

    private BadgeView mBadgeView;

    /**
     * Fragments
     */
    private FragmentManager mFragmentManager;
    private HiHomePageFragment mHomePageFragment;
    private HiGoodNightFragment mGoodNightFragment;
    private HiSleepFragment mSleepFragment;
    private HiSettingFragment mSettingFragment;

    /**
     * valuse
     */
    public static HiActivity mIntance = null;
    private AlertDialog mQuitDialog;

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
//        initDialog();
        changeFragment(getHomeFragment(), false);   //初次进入时默认加载第一页
        initButton();
        initBadgeView();
    }

    public void initViews() {
        mFragmentManager = getFragmentManager();
        mtextTitle = (TextView) findViewById(R.id.text_title);
        mbuttomGroup = (RadioGroup) findViewById(R.id.buttom_group);
        mbtnHomePage = (RadioButton) findViewById(R.id.btn_home_page);
        mbtnGoodNight = (RadioButton) findViewById(R.id.btn_good_night);
        mbtnSleep = (RadioButton) findViewById(R.id.btn_sleep);
        mbtnSetting = (RadioButton) findViewById(R.id.btn_setting);
        mbtnHomePage.setOnClickListener(this);
        mbtnGoodNight.setOnClickListener(this);
        mbtnSleep.setOnClickListener(this);
        mbtnSetting.setOnClickListener(this);
        mTitleBar = (RelativeLayout) findViewById(R.id.title_bar);
        mTitleBar.getChildAt(0).setVisibility(View.GONE);
        mTitleBar.getChildAt(2).setVisibility(View.GONE);

        mtextTitle.setText(R.string.home_page);
    }

    /**
     * 初始化按钮点击效果，默认第一页
     */
    private void initButton() {
        mbuttomGroup.check(R.id.btn_home_page);
    }

//    private void initDialog() {
//        mQuitDialogBulider = new AlertDialog.Builder(mIntance);
//        mQuitDialogBulider.setIcon()
//    }

    private void initBadgeView() {
        //底部第二个按钮
        mBadgeView = new BadgeView(this);
        mBadgeView.setBadgeCount(8);
//        mBadgeView.setBadgeMargin(5);
        mBadgeView.setBadgeMargin(7, 7, 10, 10);
//        mBadgeView.setBackgroundResource(R.color.title_color);
//        mBadgeView.setBackground(1, R.color.title_color);
        mBadgeView.setTargetView(findViewById(R.id.view_good_night));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mbtnHomePage.isChecked()) {
            changeFragment(getHomeFragment(), false);
            mtextTitle.setText(R.string.home_page);
        } else if (mbtnGoodNight.isChecked()) {
            changeFragment(getGoodNightFragment(), false);
            mtextTitle.setText(R.string.good_night);
        } else if (mbtnSleep.isChecked()) {
            changeFragment(getSleepFragment(), false);
            mtextTitle.setText(R.string.sleep);
        } else if (mbtnSetting.isChecked()) {
            changeFragment(getSettingFragment(), false);
            mtextTitle.setText(R.string.setting);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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


    /**
     * 获取fragment实例
     */
    public HiHomePageFragment getHomeFragment() {
        if (mHomePageFragment == null) {
            mHomePageFragment = new HiHomePageFragment();
        }
        return mHomePageFragment;
    }

    public HiGoodNightFragment getGoodNightFragment() {
        if (mGoodNightFragment == null) {
            mGoodNightFragment = new HiGoodNightFragment();
        }
        return mGoodNightFragment;
    }

    public HiSleepFragment getSleepFragment() {
        if (mSleepFragment == null) {
            mSleepFragment = new HiSleepFragment();
        }
        return mSleepFragment;
    }

    public HiSettingFragment getSettingFragment() {
        if (mSettingFragment == null) {
            mSettingFragment = new HiSettingFragment();
        }
        return mSettingFragment;
    }

    /**
     * 替换fragment
     */
    public void changeFragment(Fragment f, boolean init) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.frgment_root, f);
        if (!init) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    /**
     * 捕捉返回事件，弹出对话框，是否退出应用
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            showQuitDialog();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void showQuitDialog() {
        if (mQuitDialog == null) {
            mQuitDialog = new AlertDialog.Builder(this)
                    .setMessage("您确定要退出应用吗？")
                    .setPositiveButton("狠心离开", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HiMainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("再看一会", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mQuitDialog.dismiss();
                        }
                    }).create();
        }
        mQuitDialog.show();
    }
}
