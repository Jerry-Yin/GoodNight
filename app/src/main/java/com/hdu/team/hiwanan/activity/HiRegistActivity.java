package com.hdu.team.hiwanan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.constant.HiRequestCodes;
import com.hdu.team.hiwanan.listener.OnResponseListener;
import com.hdu.team.hiwanan.model.User;
import com.hdu.team.hiwanan.util.BmobNetworkUtils;
import com.hdu.team.hiwanan.util.ToastUtils;

/**
 * Created by JerryYin on 11/12/15.
 */
public class HiRegistActivity extends HiActivity {

    /**View*/
    private Button mBtnRegister;
    private EditText mTxtUsrName, mTxtPwd, mTxtPwdCfm;
    private RadioButton mBoxMan, mBoxWoman, mBoxStudent, mBoxWorker;

    /** values*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_regist);

        initViews();

    }

    private void initViews() {
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mTxtUsrName = (EditText) findViewById(R.id.txt_account);
        mTxtPwd = (EditText) findViewById(R.id.txt_password);
        mTxtPwdCfm = (EditText) findViewById(R.id.txt_pwd_confirm);
        mBoxMan = (RadioButton) findViewById(R.id.btn_man);
        mBoxWoman = (RadioButton) findViewById(R.id.btn_women);
        mBoxStudent = (RadioButton) findViewById(R.id.btn_student);
        mBoxWorker = (RadioButton) findViewById(R.id.btn_worker);
        mBtnRegister.setOnClickListener(this);
    }

    /**
     *  逻辑检查
     */
    private void checkUsr() {
        String usrName = mTxtUsrName.getText().toString().trim();
        String pwd = mTxtPwd.getText().toString().trim();
        String pwdCfm = mTxtPwdCfm.getText().toString().trim();
        if (!TextUtils.isEmpty(usrName)){
            if (!TextUtils.isEmpty(pwd) || (pwd.length()>6 && pwd.length()<15)){
                if (!TextUtils.isEmpty(pwdCfm) && pwdCfm.equals(pwd)){
                    String sex = null;
                    if (mBoxMan.isChecked()){
                        sex = getString(R.string.sex_man);
                    }else if (mBoxWoman.isChecked()){
                        sex = getString(R.string.sex_woman);
                    }else {
                        ToastUtils.showToast(this, getString(R.string.sex_not_nul), Toast.LENGTH_SHORT);
                    }
                    RegisterUsr(usrName, pwd, sex);
                }else {
                    ToastUtils.showToast(this, getString(R.string.chk_pwd_confirm), Toast.LENGTH_SHORT);
                }

            }else {
                ToastUtils.showToast(this, getString(R.string.chk_pwd), Toast.LENGTH_LONG);
            }
        }else {
            ToastUtils.showToast(this, getString(R.string.usr_null), Toast.LENGTH_SHORT);
        }

    }

    /**
     * 注册用户信息
     * @param usrName
     * @param pwd
     */
    private void RegisterUsr(String usrName, String pwd, String sex) {
        User user = new User(usrName, pwd, sex);
        final Message message = new Message();
        BmobNetworkUtils.signUp(user, new OnResponseListener() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "result = "+result);
                message.what = HiRequestCodes.REGIST_SUCCESS;
                message.obj = result;
                mHandler.sendMessage(message);
            }

            @Override
            public void onFailure(int errorCode, String error) {
                Log.d(TAG, "code = "+errorCode+" error ="+error);
                message.what = HiRequestCodes.REGIST_FAIL;
                message.obj = error;
                mHandler.sendMessage(message);
            }
        });

    }

    public void backToLogin(String name, String password){
        Intent intent = new Intent(this, HiLoginActivity.class);
        intent.putExtra(HiConfig.KEY_USER_NAME, name);
        intent.putExtra(HiConfig.KEY_PASSWORD, password);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HiRequestCodes.REGIST_SUCCESS:
                    ToastUtils.showToast(getApplicationContext(), getString(R.string.regist_success), Toast.LENGTH_LONG);
                    String name = mTxtUsrName.getText().toString().trim();
                    String password = mTxtPwd.getText().toString().trim();
                    backToLogin(name, password);
                    saveUsrInfoToLocal(name, password);
                    break;

                case HiRequestCodes.REGIST_FAIL:
                    ToastUtils.showToast(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG);
                    break;
            }
        }
    };

    /**
     * 将组册成功的用户信息保存到本地数据库
     * @param name
     * @param password
     */
    private void saveUsrInfoToLocal(String name, String password) {
        SharedPreferences.Editor editor = (SharedPreferences.Editor) getSharedPreferences(HiConfig.HI_PREFERENCE_NAME, MODE_PRIVATE);
        editor.putString(HiConfig.KEY_USER_NAME, name);
        editor.putString(HiConfig.KEY_PASSWORD, password);
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register){
            //TODO 注册用户，上传至服务器，保存至本地数据库，返回登录界面
            //需要判断用户名和密码是否符合要求规格
            checkUsr();
        }
    }
}
