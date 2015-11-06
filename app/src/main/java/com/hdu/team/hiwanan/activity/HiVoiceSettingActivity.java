package com.hdu.team.hiwanan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.util.HiConstants;
import com.hdu.team.hiwanan.util.HiToast;

/**
 * Created by JerryYin on 11/4/15.
 * 注意，此处使用PreferenceActivity，自动保存页面数据状态
 */
public class HiVoiceSettingActivity extends Activity implements View.OnClickListener {

    /**Values*/
//    public static boolean SWITCH_STATUS = false;    //标志位，用于判断switch开关的状态

    /**Views*/
    private TextView mTextTitle;
    private Switch mSwitchVoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_hi_voice);

        setupViews();
        setupSwitch();

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
        boolean switch_status = getSharedPreferences(HiConstants.HI_PREFERENCE_NAME, MODE_PRIVATE).getBoolean("hi_switch_status", false);
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
                SharedPreferences.Editor editor = getSharedPreferences(HiConstants.HI_PREFERENCE_NAME, MODE_PRIVATE).edit();
                //设置并存储switch对应的状态
                if (mSwitchVoice.isChecked()) {
                    editor.putBoolean("hi_switch_status", true);
                    HiToast.showToast(HiVoiceSettingActivity.this, R.string.switch_on_tips);

                } else {
                    editor.putBoolean("hi_switch_status", false);
                    HiToast.showToast(HiVoiceSettingActivity.this, R.string.switch_off_tips);
                }
                editor.commit();
                break;

            default:
                break;
        }
    }
}

