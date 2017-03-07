//package com.hdu.team.hiwanan.activity;
//
//import android.annotation.TargetApi;
//import android.app.AlertDialog;
//import android.app.LoaderManager;
//import android.content.Intent;
//import android.content.Loader;
//import android.content.pm.PackageManager;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.design.widget.Snackbar;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.AutoCompleteTextView;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.hdu.team.hiwanan.R;
//import com.hdu.team.hiwanan.base.HiActivity;
//
//import static android.Manifest.permission.READ_CONTACTS;
//
///**
// * Created by JerryYin on 3/7/17.
// */
//
//public class HiLoginActivityMaterial extends HiActivity {
//
//
//
//    /**
//     * Id to identity READ_CONTACTS permission request.
//     */
//    private static final int REQUEST_READ_CONTACTS = 0;
//
//
//
//    /**
//     * Keep track of the login task to ensure we can cancel it if requested.
//     */
//    private UsrLoginTask mAuthTask = null;
//
//    // UI references.
//    private AutoCompleteTextView mTxtAccount;
//    private EditText mTxtPwd;
//    private AlertDialog mDialog;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_login_material);
//
//        initview();
//    }
//
//    private void initview() {
//        mTxtAccount = (AutoCompleteTextView) findViewById(R.id.txt_account);
//        mTxtPwd = (EditText) findViewById(R.id.txt_password);
//
//
//        populateAutoComplete();
//
//        //用户输入完毕后软键盘上的回车键，可以进行登录操作
//        mTxtPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
//                if ( id == R.id.login || id == EditorInfo.IME_NULL){
//                    attemptLogin();
//                    return true;
//                }
//                return false;
//            }
//        });
//        mTxtPwd.setOnClickListener(this);
//
//        if (mDialog == null) {
//            mDialog = new AlertDialog.Builder(this).create();
//        }
//    }
//
//
//
//    /**
//     * 自动添加最近的账户名
//     */
//    private void populateAutoComplete() {
//        // TODO: 3/7/17  自动添加最近的账户名
//
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_login:
//                attemptLogin();
//                break;
//
//            default:
//                break;
//        }
//
//    }
//
//    /**
//     * 登录操作
//     */
//    private void attemptLogin() {
//        if (mDialog!= null){
//            mDialog.setMessage("登录中...");
//            mDialog.setView(mProgressBar);
//            mDialog.show();
//        }
//
//    }
//
//    /**
//     * Represents an asynchronous login/registration task used to authenticate
//     * the user.
//     * 用于用户登录的异步类
//     */
//    public class UsrLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mAccount;
//        private final String mPassword;
//
//        UsrLoginTask(String account, String password) {
//            mAccount = account;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mAccount)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }
//
//            // TODO: register the new account here.
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                finish();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//
//                    }
//                });
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }
//}
