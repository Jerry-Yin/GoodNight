package com.hdu.team.hiwanan.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.base.HiActivity;
import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.constant.HiRequestCodes;
import com.hdu.team.hiwanan.listener.OnResponseListener;
import com.hdu.team.hiwanan.model.UserBmob;
import com.hdu.team.hiwanan.service.HiLockScreenService;
import com.hdu.team.hiwanan.util.BmobNetworkUtils;
import com.hdu.team.hiwanan.util.ToastUtils;

import cn.bmob.v3.BmobUser;

/**
 * Created by JerryYin on 11/12/15.
 */
public class HiLoginActivity extends HiActivity {

    private static final String TAG = "HiLoginActivity";

    /**
     * Constant
     */
    private static final int REQUEST_CODE = 1;

    /**
     * Views
     */
    private Button mBtnLogin, mbtnRegister, mBtnLockScreen, mBtnSendBroadcast;
    private EditText mTxtAccount, mTxtPwd;
    private ImageView mBtnForgetPwd;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkUserBefore()){
            setContentView(R.layout.layout_login);
            initViews();
        }
//        HiFullScreenTools.updateFullscreenStatus(this, false);
    }

    //每次打开前先读取本地数据库，看有无用户信息，有的话直接登录
    private boolean checkUserBefore(){
//        setContentView(R.layout.layout_logo);
        SharedPreferences preferences = getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, MODE_PRIVATE);
        String usr_name = preferences.getString(HiConfig.KEY_USER_NAME, null);
        String pwd = preferences.getString(HiConfig.KEY_PASSWORD, null);
        if (!TextUtils.isEmpty(usr_name) && !TextUtils.isEmpty(pwd)) {
            UserBmob user = new UserBmob();
            user.setUsername(usr_name);
            user.setPassword(pwd);
            login(user);
            return true;
        }else {
            return false;
        }
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
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(this).create();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                //TODO 检查用户名与密码是否正确，登录，跳转至主界面，加载数据
                checkUser();
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
                Intent i2 = new Intent(Intent.ACTION_SCREEN_OFF);
//                i2.setAction()
                sendBroadcast(i2);
//                lockScreen();
                break;


            default:
                break;
        }
    }

    private void lockScreen() {
        PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK , "my tag --- wake_lock");

//        wakeLock.acquire();
        if (wakeLock != null)
            wakeLock.release();

    }


    /**
     * 逻辑检查
     */
    private void checkUser() {
        String usr_name = mTxtAccount.getText().toString().trim();
        String password = mTxtPwd.getText().toString().trim();
        if (!TextUtils.isEmpty(usr_name) && !TextUtils.isEmpty(password)) {
            UserBmob user = new UserBmob();
            user.setUsername(usr_name);
            user.setPassword(password);
            login(user);
        } else {
            ToastUtils.showToast(this, getString(R.string.chk_name_pwd), Toast.LENGTH_SHORT);
        }
    }

    /**
     * 登录
     *
     * @param user
     * @param
     */
    private void login(UserBmob user) {
        if (mDialog!= null){
            mDialog.setMessage("登录中...");
            mDialog.show();
        }
        final Message message = new Message();
        BmobNetworkUtils.signIn(user, new OnResponseListener() {
            @Override
            public void onSuccess(Object result) {
                // TODO: 7/13/16 根据当前登录用户，获取用户信息 在后面的界面中
                message.what = HiRequestCodes.LOGIN_SUCCESS;
                message.obj = result;
                mHandler.sendMessage(message);
            }

            @Override
            public void onFailure(int errorCode, String error) {
                message.what = HiRequestCodes.LOGIN_FAIL;
                message.obj = error;
                mHandler.sendMessage(message);
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HiRequestCodes.LOGIN_SUCCESS:
                    // 登录成功会返回当前已经登陆的用户
//                UserBmob userBmob = (UserBmob) result;
                    if (mDialog!= null){
                        mDialog.dismiss();
                    }
                    UserBmob user = (UserBmob) msg.obj;
                    Log.d(TAG, "user = " + user);
                    Gson gson = new Gson();
                    String json = gson.toJson(user);
                    Log.d(TAG, "json = " + json);
                    Log.d(TAG, "PWD = "+user.getObjectByKey("password"));

                    if (mTxtPwd != null){
                        String pwd = mTxtPwd.getText().toString().trim();
                        user.setPassword(pwd);
                    }
                    String pwd = user.getPassword(user);
                    if (!TextUtils.isEmpty(pwd)){
                        saveUsrInfoToLocal(user.getUsername(), pwd);
                    }else {
//                        // TODO: 7/15/16  从服务器返回的用户信息保存 密码如何获取？
//
                    }

                    Intent intent = new Intent(HiLoginActivity.this, HiMainActivity.class);
                    startActivity(intent);
                    HiLoginActivity.this.finish();
                    break;
                case HiRequestCodes.LOGIN_FAIL:
                    if (mDialog != null){
                        mDialog.dismiss();
                    }
                    ToastUtils.showToast(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
            }
        }
    };


    /**
     * 将登录成功的用户信息保存到本地数据库方便下次直接本地读取登录
     * @param name
     * @param password
     */
    private void saveUsrInfoToLocal(String name, String password) {
        SharedPreferences.Editor editor = getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, MODE_PRIVATE).edit();
        editor.putString(HiConfig.KEY_USER_NAME, name);
        editor.putString(HiConfig.KEY_PASSWORD, password);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String userName = data.getStringExtra(HiConfig.KEY_USER_NAME);
                    String password = data.getStringExtra(HiConfig.KEY_PASSWORD);
                    mTxtAccount.setText(userName);
                    mTxtAccount.setSelection(userName.length());
                    mTxtPwd.setText(password);
                }
                break;
            default:
                break;
        }

    }
}
