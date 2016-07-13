package com.hdu.team.hiwanan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.service.HiLockScreenService;

/**
 * Created by JerryYin on 11/12/15.
 */
public class HiLoginActivity extends HiActivity{


    /**Constant*/
    private static final int REQUEST_CODE = 1;

    /**Views*/
    private Button mBtnLogin, mbtnRegister, mBtnLockScreen, mBtnSendBroadcast;
    private EditText mTxtAccount, mTxtPwd;
    private ImageView mBtnForgetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        initViews();
//        HiFullScreenTools.updateFullscreenStatus(this, false);
    }

    private void initViews() {
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mbtnRegister = (Button) findViewById(R.id.btn_to_register);
        mBtnForgetPwd = (ImageView) findViewById(R.id.img_forget_pwd);
        mTxtAccount = (EditText) findViewById(R.id.txt_account);
        mTxtPwd = (EditText) findViewById(R.id.txt_password);
        mBtnLogin.setOnClickListener(this);
        mbtnRegister.setOnClickListener(this);
        mBtnForgetPwd.setOnClickListener(this);

        mBtnLockScreen = (Button) findViewById(R.id.btn_start_lock_service);
        mBtnSendBroadcast = (Button) findViewById(R.id.btn_send_broadcast);
        mBtnLockScreen.setOnClickListener(this);
        mBtnSendBroadcast.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                //TODO 检查用户名与密码是否正确，登录，跳转至主界面，加载数据


                Intent go = new Intent(this, HiMainActivity.class);
                startActivity(go);
                this.finish();
                break;

            case R.id.btn_to_register:
                Intent intent = new Intent(this, HiRegistActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;

            case R.id.img_forget_pwd:

                break;

            case R.id.btn_start_lock_service:
                Intent intent1 = new Intent(this, HiLockScreenService.class);
                startService(intent1);
                break;

            case R.id.btn_send_broadcast:
                // 发送锁屏广播（显示锁屏界面）
//                Intent i2 = new Intent(Intent.ACTION_SCREEN_OFF);
////                i2.setAction()
//                sendBroadcast(i2);
                break;


            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    String userName = data.getStringExtra(HiConfig.KEY_USER_NAME);
                    String password = data.getStringExtra(HiConfig.KEY_PASSWORD);
                    mTxtAccount.setText(userName);
                    mTxtPwd.setText(password);
                }
                break;
            default:
                break;
        }

    }
}
