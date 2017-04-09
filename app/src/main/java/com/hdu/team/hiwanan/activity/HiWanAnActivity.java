package com.hdu.team.hiwanan.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.base.HiActivity;
import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.constant.HiRequestCodes;
import com.hdu.team.hiwanan.listener.OnProgressListener;
import com.hdu.team.hiwanan.manager.HiMediaPlayerManager;
import com.hdu.team.hiwanan.model.RecorderVoice;
import com.hdu.team.hiwanan.network.BmobNetworkUtils;
import com.hdu.team.hiwanan.util.common.HiTimesUtil;
import com.hdu.team.hiwanan.util.common.AmrFileUtil;
import com.hdu.team.hiwanan.util.common.FileUtils;
import com.hdu.team.hiwanan.view.HiVoiceRecorderButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiWanAnActivity extends HiActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private final static String TAG = "HiWanAnActivity";

    /**
     * Views
     */
    private TextView mTextTitle;
    private ImageView mbtnBack;
    //    private Button mbtnSpeak;
    private ListView mlistVoice;
    private HiVoiceRecorderButton mbtnSpeak;


    /**
     * Values
     */
    private Vibrator mVibrator;
    private ArrayAdapter<RecorderVoice> mAdapter;
    private List<RecorderVoice> mDataLists = new ArrayList<>();
    private View mAnimView;

    private ArrayList<Boolean> mCancelAble = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_hi_goodnight);
        setContentView(R.layout.layout_hi_goodnight2);

        initViews();
    }

    private void initViews() {
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mbtnBack = (ImageView) findViewById(R.id.btn_title_back);
        mTextTitle.setText(R.string.hiwanan);
        mbtnBack.setOnClickListener(this);
        mlistVoice = (ListView) findViewById(R.id.list_voice_item);
        mAdapter = new HiVoiceListAdapter(this, mDataLists);
        mlistVoice.setAdapter(mAdapter);
        mbtnSpeak = (HiVoiceRecorderButton) findViewById(R.id.btn_speak);
        mbtnSpeak.setAudioFinishRecorderListener(new HiVoiceRecorderButton.OnFinishRecorderListener() {
            @Override
            public void onFinish(float times, String filePath) {
                //录音完成调用
                RecorderVoice recorderVoice = new RecorderVoice(filePath, times, HiTimesUtil.getCurDateTime());
                mDataLists.add(recorderVoice);
                mAdapter.notifyDataSetChanged();
                int position = mDataLists.size()-1;
                mlistVoice.setSelection(position);

                //TODO 录音完毕 发送音频到服务器； 启动撤回倒计时线程，
                sendVoiceToServer(mDataLists.size()-1);
                mCancelAble.add(true);
                HiTimesUtil.startCountDown(times*2, mHandler, position);
            }
        });
        mlistVoice.setOnItemClickListener(this);
        mlistVoice.setOnItemLongClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_title_back:
                this.finish();
                break;

            case R.id.btn_speak:

                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //为防止在播放一个item 的时候 点击了另外一个item
        if (mAnimView != null) {
            mAnimView.setBackgroundResource(R.drawable.img_anim);
            mAnimView = null;
        }
        //TODO 播放动画
        mAnimView = view.findViewById(R.id.img_record_anim);
        mAnimView.setBackgroundResource(R.drawable.play_voice_anim);
        AnimationDrawable animationDrawable = (AnimationDrawable) mAnimView.getBackground();
        animationDrawable.start();

        //TODO 播放音频
        HiMediaPlayerManager.playSound(mDataLists.get(position).filePath, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //TODO 播放完成后调用
                mAnimView.setBackgroundResource(R.drawable.img_anim);
            }
        });
    }

    /**
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (mCancelAble.get(position)){
            mBuilder.setItems(new CharSequence[]{"复制", "撤回", "删除", "更多..."}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:

                            break;

                        case 1:

                            break;

                        case 2:
                            FileUtils.deleteFile(mDataLists.get(position).filePath);
                            reloadLocalVoice();
                            break;

                        case 3:

                            break;
                    }
                }
            }).create().show();
        }else {
            mBuilder.setItems(new CharSequence[]{"复制", "删除", "更多..."}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:

                            break;

                        case 1:
                            FileUtils.deleteFile(mDataLists.get(position).filePath);
                            reloadLocalVoice();
                            break;

                        case 2:

                            break;
                    }
                }
            }).create().show();
        }

        return true;
    }

    public void sendVoiceToServer(int position){
        BmobNetworkUtils.uploadFile(mDataLists.get(position).filePath, new OnProgressListener() {
            @Override
            public void onSuccess(Object result) {
                Log.d(TAG, "result = "+result.toString());
                // result = http://bmob-cdn-4793.b0.upaiyun.com/2016/07/19/e1d1a9b35a3848a4964ec9af51fa264b.amr

            }

            @Override
            public void onFailure(int errorCode, String error) {
                Log.d(TAG, "error = "+errorCode+ " " +error.toString());
            }

            @Override
            public void onProgress(Integer progress) {
                Log.d(TAG, "progress = "+progress);
            }
        });
    }

    /**
     * 适配器 ArrayAdapter<RecorderVoice>
     */
    public class HiVoiceListAdapter extends ArrayAdapter<RecorderVoice> {
        private List<RecorderVoice> mDatas;
        private Context mContext;
        private LayoutInflater mLayoutInflater;

        //语音item的最大最小宽度，根据屏幕宽度确定
        private int itemMinWidth;
        private int itemMaxWidth;


        private class ViewHolder {
            TextView seconds;   //时长秒数
            View length;        //item条目宽度
        }

        public HiVoiceListAdapter(Context context, List<RecorderVoice> datas) {
            super(context, -1, datas);
            mLayoutInflater = LayoutInflater.from(context);
            //获取屏幕宽度
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(metrics);
            itemMaxWidth = (int) (metrics.widthPixels * 0.7f);
            itemMinWidth = (int) (metrics.widthPixels * 0.2f);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.layout_item_record_voice, parent, false);
                holder = new ViewHolder();
                holder.seconds = (TextView) convertView.findViewById(R.id.text_voice_time);
                holder.length = convertView.findViewById(R.id.zone_voice_msg);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.seconds.setText(Math.round(getItem(position).time) + "\"");
            ViewGroup.LayoutParams lp = holder.length.getLayoutParams();
            lp.width = (int) (itemMinWidth + itemMaxWidth / 60f * getItem(position).time);  //此处设置最大时长为60s

            return convertView;
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HiRequestCodes.COUNT_DOWN:
                    //倒计时
                    Log.d(TAG, "倒计时："+msg.obj + " s");
                    Log.d(TAG, "mCancelAble.get(position) = "+mCancelAble.get(msg.arg1));
                    if (Float.valueOf(msg.obj.toString()) <= 0){
                        mCancelAble.set(msg.arg1, false);
                        Log.d(TAG, "mCancelAble.get(position) done = "+mCancelAble.get(msg.arg1));
                    }
                    break;
            }
        }
    };

    //加载之前的本地录音
    private void reloadLocalVoice() {
        ArrayList<File> localList = FileUtils.readFile(HiConfig.APP_VOICE_DIR);
        mCancelAble.clear();
        mDataLists.clear();
        mAdapter.notifyDataSetChanged();
        for (File file : localList){
            Log.d(TAG, "path = "+file.getPath());
            Log.d(TAG, "abPath = "+file.getAbsolutePath());
            Log.d(TAG, "file = "+file.getName());
            String[] names = file.getName().split("\\|");
            for (String n : names){
                //name : date.time.amr
                Log.d(TAG, "n = "+n);
            }

            long duration = 0;
            try {
                duration = AmrFileUtil.getAmrDuration(file);
                Log.d(TAG, "duration = "+ duration);

            } catch (IOException e) {
                e.printStackTrace();
            }

            RecorderVoice voice = new RecorderVoice(file.getPath(), duration, names[0]);
            mDataLists.add(voice);
            mCancelAble.add(false);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        HiMediaPlayerManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HiMediaPlayerManager.resume();

        reloadLocalVoice();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mVibrator.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HiMediaPlayerManager.release();
    }
}
