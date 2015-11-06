package com.hdu.team.hiwanan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;

/**
 * Created by JerryYin on 11/4/15.
 */
public class HiAboutFunction extends HiActivity {

    private TextView mTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_about_app);

        initView();
    }

    private void initView() {
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextTitle.setText(R.string.about_app);

    }

    @Override
    public void onClick(View v) {

    }
}
