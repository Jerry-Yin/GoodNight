package com.hdu.team.hiwanan.activity;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
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
import com.hdu.team.hiwanan.manager.HiMediaPlayerManager;
import com.hdu.team.hiwanan.util.HiConstants;
import com.hdu.team.hiwanan.util.HiLog;
import com.hdu.team.hiwanan.utils.HiUploadAudioUtil;
import com.hdu.team.hiwanan.view.HiVoiceRecorderButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiWanAnActivity extends HiActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

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
                RecorderVoice recorderVoice = new RecorderVoice(filePath, times);
                mDataLists.add(recorderVoice);
                mAdapter.notifyDataSetChanged();
                mlistVoice.setSelection(mDataLists.size() - 1);
            }
        });
        mlistVoice.setOnItemClickListener(this);
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
    protected void onStop() {
        super.onStop();
//        mVibrator.cancel();
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

        //TODO 传送音频到服务器
        String result = HiUploadAudioUtil.upLoadAudio(new File(mDataLists.get(position).filePath), HiConstants.TEST_URL);
        HiLog.d(TAG, "result = "+result);

    }

    /**
     * 记录录音voice条目的类
     */
    class RecorderVoice {
        String filePath;
        float time;


        public RecorderVoice(String filePath, float time) {
            super();
            this.filePath = filePath;
            this.time = time;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public float getTime() {
            return time;
        }

        public void setTime(float time) {
            this.time = time;
        }
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
                convertView = mLayoutInflater.inflate(R.layout.item_record_voice, parent, false);
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

    private class ViewHolder {
        TextView seconds;   //时长秒数
        View length;        //item条目宽度
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HiMediaPlayerManager.release();
    }
}
