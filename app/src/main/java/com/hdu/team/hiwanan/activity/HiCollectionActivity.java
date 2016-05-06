package com.hdu.team.hiwanan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.hdu.team.hiwanan.R;

/**
 * Created by JerryYin on 11/23/15.
 */
public class HiCollectionActivity extends HiActivity{

    private static final int ADD_COLL = 1;
    /**Views*/
    private ListView mListCollection;
    private Button mBtnAdd, mBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hi_collection);

        initViews();

    }

    private void initViews() {
        mBtnAdd = (Button) findViewById(R.id.btn_add);
//        mBtnBack = (Button) findViewById(R.id.btn_title_back);
//        mBtnBack.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                Intent intent = new Intent(this, HiWanAnShareActivity2.class);
//                startActivityForResult(intent, ADD_COLL);
                startActivity(intent);
//                this.finish();
                break;

            case R.id.btn_title_back:
                this.finish();
                break;

            default:
                break;
        }
    }
}
