package com.hdu.team.hiwanan.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.util.HiLog;
import com.hdu.team.hiwanan.utils.HiCameraUtil;
import com.hdu.team.hiwanan.util.HiToast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JerryYin on 11/13/15.
 */
public class HiWanAnShareActivity extends HiActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    /**
     * Constants
     */
    private static final String TAG = "HiWanAnShareActivity";
    private static final int IMAGE_OPEN = 1;
    private static final int TAKE_PHOTO = 2;

    /**
     * Views
     */
    private GridView mGridViewImg;
    private EditText mEtShare;

    /**
     * Values
     */
    private List<Map<String, Object>> mDataList;
    private SimpleAdapter mSimpleAdapter;
    private Bitmap mBitmap;
    private String pathImage;   //代显示的图片路径（本地图片＋照片）
    private Uri mImgFileUri;        //拍照的照片保存到本地的SD_card的 Pictures/HiWanAnCamera/ 路径下；


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hi_share);

        initViews();
        initData();

    }

    private void initViews() {
        mGridViewImg = (GridView) findViewById(R.id.grid_view_share);
        mEtShare = (EditText) findViewById(R.id.edit_text_share);
        mGridViewImg.setOnItemClickListener(this);
        mGridViewImg.setOnItemLongClickListener(this);
    }

    public void initData() {
        mDataList = new ArrayList<>();

        /** 1.可行，直接传入drawable*/
//        Map<String, Object> map = new HashMap<>();
//        map.put("itemImg", R.drawable.add_img);

        /** 2.采用bitmap */
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add_img);
        Map<String, Object> map = new HashMap<>();
        map.put("itemImg", mBitmap);

        mDataList.add(map);
        mSimpleAdapter = new SimpleAdapter(this, mDataList, R.layout.gridview_item, new String[]{"itemImg"}, new int[]{R.id.img_grid_view_item});
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         * 解决方法:
         *              1.自定义继承BaseAdapter实现
         *              2.ViewBinder()接口实现
         *  参考 http://blog.csdn.net/admin_/article/details/7257901
         */
        mSimpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView img = (ImageView) view;
                    img.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });

        mGridViewImg.setAdapter(mSimpleAdapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mDataList.size() >= 10) {
            HiToast.showToast(this, R.string.just_9_img);
        } else if (position == 0) {
//            HiToast.showToast(this, R.string.add_picture);
            //TODO 选择图片（本地图片 或者 拍照）

            char[] a = new char[2];
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("添加图片")
                    .setItems(new CharSequence[]{"拍照", "选择本地照片"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
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

        } else {

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

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("itemImg", bitmap);
                    mDataList.add(map);
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
        //TODO 通过onResume()刷新数据
        if (!TextUtils.isEmpty(pathImage)) {
            mBitmap = BitmapFactory.decodeFile(pathImage);
            HashMap<String, Object> map = new HashMap<>();
            map.put("itemImg", mBitmap);
            mDataList.add(map);
            mSimpleAdapter.notifyDataSetChanged();

            /**
             * 开启一个线程 （否则会卡顿）
             * 删除应用内的拍照照片
             * */
//            new Runnable() {
//                @Override
//                public void run() {
//                    File imgFile = new File(pathImage);
//                    imgFile.delete();
//                }
//            }.run();
            /** 刷新后释放防止手机休眠后自动添加*/
            pathImage = null;
        }
        mSimpleAdapter.notifyDataSetChanged();
    }

    /**
     * 长按移除图片
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (position != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("确认移除已添加图片吗？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mDataList.remove(position);
                    mSimpleAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
        return false;
    }


    /**
     * 释放资源
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
