package com.hdu.team.hiwanan.listener;

import android.media.MediaPlayer;

/**
 * Created by JerryYin on 4/27/17.
 */

public interface OnPlayingListener extends MediaPlayer.OnCompletionListener{

    void onProgress(int progress);

    @Override
    void onCompletion(MediaPlayer mp);


}

