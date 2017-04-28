package com.hdu.team.hiwanan.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.base.HiActivity;
import com.hdu.team.hiwanan.listener.OnPlayingListener;
import com.hdu.team.hiwanan.manager.HiMediaPlayerManager;
import com.hdu.team.hiwanan.util.HiLog;

import co.mobiwise.library.MusicPlayerView;

/**
 * Created by JerryYin on 4/27/17.
 */

public class HiMusicPlayerActivity extends HiActivity {

    /**
     * Constants
     */
    private static final String TAG = "HiMusicPlayerActivity";

    /**
     * View
     */
    private MusicPlayerView mMusicView;

    /**
     * Valuse
     */
    private HiMediaPlayerManager mPlayerManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_music);

        initViews();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initMusic();
    }

    private void initViews() {
        mMusicView = (MusicPlayerView) findViewById(R.id.music_view);

        mMusicView.setOnClickListener(this);
    }

    private void initMusic() {
        if (mPlayerManager == null)
            mPlayerManager = HiMediaPlayerManager.getMediaPlayer(this);
        HiLog.d(TAG, mPlayerManager.toString());
        HiLog.d(TAG, "isPlaying: "+ mPlayerManager.isPlaying());
        HiLog.d(TAG, "isPaused: "+ mPlayerManager.isPaused());

        //playing
        if (mPlayerManager.isPlaying()) {
            mMusicView.setMax(mPlayerManager.getMaxProgress());
            mMusicView.setProgress(mPlayerManager.getProgress());
            mMusicView.start();
            mPlayerManager.setOnPlayingListener(new OnPlayingListener() {
                @Override
                public void onProgress(int progress) {
//                    mMusicView.setProgress(mPlayerManager.getProgress());
                }

                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!mMusicView.isRotating()) {
                        mMusicView.stop();
                    }
                }
            });
        }else if (mPlayerManager.isPaused()){
            //paused
            mMusicView.setMax(mPlayerManager.getMaxProgress());
            mMusicView.setProgress(mPlayerManager.getProgress());
//            mMusicView.stop();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAfterTransition(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music_view:
                if (mPlayerManager != null && mPlayerManager.isPaused()) {
                    resumeMusic();
                } else {
                    playMusic();
                }
                break;

            default:
                break;
        }
    }

    private void resumeMusic() {
        mPlayerManager.resume();
        mMusicView.start();
    }


    private void playMusic() {
        if (!mMusicView.isRotating()) {
            mPlayerManager.playSound(null, R.raw.jaychou1, new OnPlayingListener() {
                @Override
                public void onProgress(int progress) {
//                    mMusicView.setProgress(progress);
                }

                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!mMusicView.isRotating()) {
                        mMusicView.stop();
                    }
                }
            });
            mMusicView.setMax(mPlayerManager.getMaxProgress());
            mMusicView.start();
        } else {
            mPlayerManager.pause();
            mMusicView.stop();
        }
    }


}
