package com.hdu.team.hiwanan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.view.HiLoadingDialogManager;

/**
 * Created by JerryYin on 11/12/15.
 */
public class HiRegistActivity extends Activity {

    public static final String KEY_USER_NAME = "KEY_USER_NAME";

    /**View*/
    private Button mBtnRegister;
    private EditText mTxtUsrName, mTxtPwd;
    private CheckBox mBoxMan, mBoxWoman, mBoxStudent, mBoxWorker;


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
        mBoxMan = (CheckBox) findViewById(R.id.checkbox_sex_man);
        mBoxWoman = (CheckBox) findViewById(R.id.checkbox_sex_woman);
        mBoxStudent = (CheckBox) findViewById(R.id.checkbox_type_student);
        mBoxWorker = (CheckBox) findViewById(R.id.checkbox_type_worker);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 注册用户，上传至服务器，保存至本地数据库，返回登录界面
                //需要判断用户名和密码是否符合要求规格

                backToLogin();
            }
        });
    }

    public void backToLogin(){
        Intent intent = new Intent(this, HiLoginActivity.class);
        intent.putExtra(KEY_USER_NAME, mTxtUsrName.getText().toString());
        setResult(RESULT_OK, intent);
        this.finish();
    }
}
