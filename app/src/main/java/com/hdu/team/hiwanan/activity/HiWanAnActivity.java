package com.hdu.team.hiwanan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;

/**
 * Created by JerryYin on 11/3/15.
 */
public class HiWanAnActivity extends HiActivity implements View.OnClickListener {


    /**Views*/
    private TextView mTextTitle;
    private ImageView mbtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hi_goodnight);

        initViews();
    }

    private void initViews() {
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mbtnBack = (ImageView) findViewById(R.id.btn_title_back);
        mTextTitle.setText(R.string.hiwanan);
        mbtnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_title_back:
                this.finish();
                break;

            default:
                break;
        }
    }
}
