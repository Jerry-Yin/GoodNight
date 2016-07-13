package com.hdu.team.hiwanan.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.util.HiToast;

/**
 * Created by JerryYin on 11/4/15.
 * 注意，此处使用PreferenceActivity，自动保存页面数据状态
 */
public class HiVoiceSettingActivity extends HiActivity {

    /**
     * Values
     */
    private final String[] mItemSex = new String[]{"只看异性", "只看同性", "任意性别"};
    private SharedPreferences.Editor mEditor;
    /**
     * Views
     */
    private TextView mTextTitle;
    private Switch mSwitchVoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_hi_voice);

        setupViews();
        initData();
        setupSwitch();

        int[] a = new int[3];
    }

    private void initData() {
        mEditor = getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, MODE_PRIVATE).edit();
    }

    private void setupViews() {
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextTitle.setText(R.string.setting_hi);
        mSwitchVoice = (Switch) findViewById(R.id.switch_voice);
        mSwitchVoice.setOnClickListener(this);
    }

    /**
     * 界面初始化switch开关状态
     */
    private void setupSwitch() {
        boolean switch_status = getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, MODE_PRIVATE).getBoolean("hi_switch_status", false);
        if (switch_status) {
            mSwitchVoice.setChecked(true);
        } else {
            mSwitchVoice.setChecked(false);
        }
    }

    /**
     * 确认对持久性数据的未保存更改、停止动画以及其他可能消耗 CPU 的内容，诸如此类
     */
    @Override
    protected void onPause() {
        super.onPause();
        //保存页面当前数据状态
//        if (SWITCH_STATUS){
//            mSwitchVoice.setChecked(true);
//        }else {
//            mSwitchVoice.setChecked(false);
//        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_voice:

                //设置并存储switch对应的状态
                if (mSwitchVoice.isChecked()) {
                    mEditor.putBoolean("hi_switch_status", true);
//                    HiToast.showToast(HiVoiceSettingActivity.this, R.string.switch_on_tips);
                    showDialog();

                } else {
                    mEditor.putBoolean("hi_switch_status", false);
                    HiToast.showToast(HiVoiceSettingActivity.this, R.string.switch_off_tips);
                }
                mEditor.commit();
                break;

            default:
                break;
        }
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hi语音类别选择")
                .setItems(mItemSex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                HiToast.showToast(HiVoiceSettingActivity.this, R.string.sex_diff_choosed);
                                mEditor.putBoolean(HiConfig.KEY_SEX_DIF, true);
                                mEditor.putBoolean(HiConfig.KEY_SEX_SAME, false);
                                mEditor.putBoolean(HiConfig.KEY_SEX_ALL, false);
                                mEditor.commit();
                                break;

                            case 1:
                                HiToast.showToast(HiVoiceSettingActivity.this, R.string.sex_same_choosed);
                                mEditor.putBoolean(HiConfig.KEY_SEX_DIF, false);
                                mEditor.putBoolean(HiConfig.KEY_SEX_SAME, true);
                                mEditor.putBoolean(HiConfig.KEY_SEX_ALL, false);
                                mEditor.commit();
                                break;

                            case 2:
                                HiToast.showToast(HiVoiceSettingActivity.this, R.string.sex_all_choosed);
                                mEditor.putBoolean(HiConfig.KEY_SEX_DIF, false);
                                mEditor.putBoolean(HiConfig.KEY_SEX_SAME, false);
                                mEditor.putBoolean(HiConfig.KEY_SEX_ALL, true);
                                mEditor.commit();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setCancelable(false)
                .create().show();
    }
}

