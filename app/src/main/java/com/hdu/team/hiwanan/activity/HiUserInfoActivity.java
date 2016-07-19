package com.hdu.team.hiwanan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.base.HiActivity;
import com.hdu.team.hiwanan.constant.HiRequestCodes;
import com.hdu.team.hiwanan.listener.OnProgressListener;
import com.hdu.team.hiwanan.listener.OnResponseListener;
import com.hdu.team.hiwanan.model.UserBmob;
import com.hdu.team.hiwanan.util.BmobNetworkUtils;
import com.hdu.team.hiwanan.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.utils.L;

import java.util.HashMap;

import cn.bmob.v3.BmobUser;

public class HiUserInfoActivity extends HiActivity {

    private static final String TAG = "HiUserInfoActivity";

    /**
     * Views
     */
    private RelativeLayout mBtnIcon;
    private ImageView mImgIcon;
    private TextView mTxtName;

    private AlertDialog.Builder mBuilder;
    private AlertDialog mAlertDialog;


    /**
     * Values
     */
    private String mImgPath;
    private UserBmob mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_info);

        initViews();
        initDatas();
    }

    private void initViews() {
        mBtnIcon = (RelativeLayout) findViewById(R.id.btn_icon);
        mImgIcon = (ImageView) findViewById(R.id.img_account);
        mTxtName = (TextView) findViewById(R.id.txt_usr_name);
        mBtnIcon.setOnClickListener(this);

        if (mBuilder == null) {
            mBuilder = new AlertDialog.Builder(this);
        }
        if (mAlertDialog == null) {
            mAlertDialog = mBuilder.create();
        }
    }


    private void initDatas() {
        mUser = BmobUser.getCurrentUser(UserBmob.class);
        loadUsrInfo();
    }

    /**
     * 加载用户信息
     */
    private void loadUsrInfo() {
        UserBmob usr = BmobUser.getCurrentUser(UserBmob.class);
        Log.d(TAG, "curUsr = " + usr);
        Log.d(TAG, "name = " + usr.getUsername());
        Log.d(TAG, "icon = " + usr.getIcon());    //用户头像url

        ImageLoaderUtil.displayWebImage(usr.getIcon(), mImgIcon);   // 加载用户头像
        mTxtName.setText(usr.getUsername());
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_icon:
                // TODO: 7/16/16 更换用户头像
                mBuilder.setTitle("更换头像")
                        .setItems(new CharSequence[]{"拍照", "选择本地照片"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        //系统相机拍照
                                        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    mImgFileUri = HiCameraUtil.getOutputMediaFileUri(HiCameraUtil.MEDIA_TYPE_IMAGE);  //create a file to save the image
//                                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, mImgFileUri);     //set the image file name
                                        startActivityForResult(intent1, HiRequestCodes.TAKE_PHOTO);
                                        break;
                                    case 1:
                                        //选择本地照片
                                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(intent, HiRequestCodes.IMAGE_OPEN);
                                        break;
                                }
                            }
                        })
                        .create().show();

                break;

            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case HiRequestCodes.IMAGE_OPEN:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (!TextUtils.isEmpty(uri.getAuthority())) {
                        //查询选择图片
                        Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                        //返回 没找到选择图片
                        if (null == cursor) {
                            return;
                        }
                        //光标移动至开头 获取图片路径
                        cursor.moveToFirst();
                        mImgPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        cursor.close();

                        uploadIcon(mImgPath, null);
                    }
                }
                break;

            case HiRequestCodes.TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
//                    Intent i = getIntent();
//                    HiLog.d(TAG, i.getData().toString());

//                    HiToast.showToast(this, TAG + "Image saved to:\n"+data.getData());
//                    HiLog.d(TAG, "Image saved to:\n" + data.getData());
//                    HiLog.d(TAG, "bitmap =\n"+data.getExtras().get("data").toString());
//                    HiLog.d(TAG, "Image saved to:\n"+data.getData());

//                    pathImage = mImgFileUri.getEncodedPath();

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                }
                break;
            default:
                break;
        }
    }

    /**
     * 上传文件到服务器
     *
     * @param mImgPath
     * @param bitmap
     */
    private void uploadIcon(String mImgPath, Bitmap bitmap) {
        mAlertDialog.setMessage("上传头像...");
        mAlertDialog.show();
        final Message msg = new Message();
        if (mImgPath != null) {
            BmobNetworkUtils.uploadFile(mImgPath, new OnProgressListener() {
                @Override
                public void onSuccess(Object result) {
                    if (!TextUtils.isEmpty(result.toString())) {
                        Log.d(TAG, "上传成功，url = " + result.toString());
                        msg.what = HiRequestCodes.UPLOAD_SUCCESS;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                }

                @Override
                public void onFailure(int errorCode, String error) {
                    Log.d(TAG, "CODE =" + errorCode + " error = " + error);

                }

                @Override
                public void onProgress(Integer progress) {
                    Log.d(TAG, "progress = " + progress);

                }
            });
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HiRequestCodes.UPLOAD_SUCCESS:
                    mAlertDialog.dismiss();
                    mUser.setIcon(msg.obj.toString());
                    updateUser(msg.obj.toString());
                    break;

                case HiRequestCodes.UPLOAD_PROGRESS:

                    break;

                case HiRequestCodes.UPLOAD_FAIL:

                    break;
            }

        }
    };

    private void updateUser(String iconPath) {
        ImageLoaderUtil.displayFileImage(mImgPath, mImgIcon);   // 加载用户头像
        BmobNetworkUtils.updateUser(null, null, iconPath, 0, null, new OnResponseListener() {
            @Override
            public void onSuccess(Object result) {
                Log.d(TAG, result.toString());
            }

            @Override
            public void onFailure(int errorCode, String error) {
                Log.d(TAG, errorCode + "error: "+error);
            }
        });
    }
}

