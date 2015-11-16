package com.hdu.team.hiwanan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hdu.team.hiwanan.R;

/**
 * Created by JerryYin on 11/16/15.
 */
public class HiLogoActivity extends HiActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_logo);

    }


    @Override
    protected void onResume() {
        super.onResume();

//        startActivity(new Intent(this, HiLogoActivity.class));
//        this.finish();
    }

    @Override
    public void onClick(View v) {

    }
}
