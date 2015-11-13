package com.hdu.team.hiwanan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.view.HiLoadingDialogManager;

/**
 * Created by JerryYin on 11/12/15.
 */
public class HiLoginActivity extends Activity implements View.OnClickListener {


    /**Constant*/
    private static final int RESULT_CODE = 1;

    /**Views*/
    private Button mBtnLogin, mbtnRegister;
    private EditText mTxtAccount, mTxtPwd;
    private ImageView mBtnForgetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        initViews();
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
                startActivityForResult(intent, RESULT_CODE);
                break;

            case R.id.img_forget_pwd:

                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RESULT_CODE:
                if (resultCode == RESULT_OK){
                    String userName = data.getStringExtra(HiRegistActivity.KEY_USER_NAME);
                    mTxtAccount.setText(userName);
                }
                break;
            default:
                break;
        }

    }
}
