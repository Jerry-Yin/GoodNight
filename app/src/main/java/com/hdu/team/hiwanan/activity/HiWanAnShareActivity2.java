package com.hdu.team.hiwanan.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.hdu.team.hiwanan.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JerryYin on 11/20/15.
 */
public class HiWanAnShareActivity2 extends HiActivity {

    private static final int IMAGE_OPEN = 1;
    private static final int TAKE_PHOTO = 2;

    private EditText mTxtShare;
    private Button mBtnAddPic, mBtnSave;

    private Bitmap mBitmap;

    private List<Map<String, Object>> mDataList;
    private String pathImage;   //代显示的图片路径（本地图片＋照片）


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hi_share2);
        initViews();

    }

    private void initViews() {
        mTxtShare = (EditText) findViewById(R.id.edit_text_share);
        mBtnAddPic = (Button) findViewById(R.id.btn_add_pic);
        mBtnSave = (Button) findViewById(R.id.btn_title_save);
        mBtnAddPic.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_pic:
                //TODO 选择图片（本地图片 或者 拍照）
                char[] a = new char[2];
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("添加图片")
                        .setItems(new CharSequence[]{"拍照", "选择本地照片"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        //系统相机拍照
                                        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    mImgFileUri = HiCameraUtil.getOutputMediaFileUri(HiCameraUtil.MEDIA_TYPE_IMAGE);  //create a file to save the image
//                                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, mImgFileUri);     //set the image file name
                                        startActivityForResult(intent1, TAKE_PHOTO);
                                        break;
                                    case 1:
                                        //选择本地照片
                                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(intent, IMAGE_OPEN);
                                        break;
                                }
                            }
                        })
                        .create().show();
                //TODO 通过onResume()刷新数据
                break;

            case R.id.btn_title_save:
                //TODO 保存编辑区域内容

                break;

            case R.id.btn_title_back:
                this.finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_OPEN:
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
                        pathImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        cursor.close();
                    }
                }
                break;

            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
//                    Intent i = getIntent();
//                    HiLog.d(TAG, i.getData().toString());

//                    HiToast.showToast(this, TAG + "Image saved to:\n"+data.getData());
//                    HiLog.d(TAG, "Image saved to:\n" + data.getData());
//                    HiLog.d(TAG, "bitmap =\n"+data.getExtras().get("data").toString());
//                    HiLog.d(TAG, "Image saved to:\n"+data.getData());

//                    pathImage = mImgFileUri.getEncodedPath();

                    Bitmap originalBitmap = (Bitmap) data.getExtras().get("data");
                    //设置图片宽高
//                    mBitmap =
                }
                break;
            default:
                break;
        }
    }

    //刷新添加后的图片
    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(pathImage)) {
            Bitmap bitmap = BitmapFactory.decodeFile(pathImage);

//            int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
//
//            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//            int width = wm.getDefaultDisplay().getWidth();

//            DisplayMetrics dm = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(dm);
//            int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
//            int mScreenHeight = dm.heightPixels;

            mBitmap = resizeBitmap(bitmap, bitmap.getWidth()*0.7f, bitmap.getHeight()*0.7f);

            SpannableString spannableString = new SpannableString(pathImage);
            ImageSpan span = new ImageSpan(mBitmap, ImageSpan.ALIGN_BASELINE);
//            ImageSpan span = new ImageSpan(mBitmap, ImageSpan.ALIGN_BASELINE);
            spannableString.setSpan(span, 0, pathImage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            //在原有内容的基础上插入图片
            Editable et = mTxtShare.getText();
            int start = mTxtShare.getSelectionStart();
            et.insert(start, spannableString);  //设置添加位置
            mTxtShare.setText(et);  //添加
            mTxtShare.setSelection(start + spannableString.length()); //光标位置移动到最后

            //初始化
            pathImage = null;
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, float width, float height) {
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, (int)width, (int)height, true);
        return bitmap1;
    }

}
