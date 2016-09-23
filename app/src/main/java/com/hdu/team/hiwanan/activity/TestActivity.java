package com.hdu.team.hiwanan.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.base.HiActivity;

/**
 * Created by JerryYin on 9/18/16.
 *
 * 用于测试MaterialViewPager
 */
public class TestActivity extends HiActivity {


    private MaterialViewPager materialViewPager;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);


        materialViewPager = (MaterialViewPager) findViewById(R.id.material_viewpager);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

//        mRecyclerView.setDecorator(new MaterialViewPagerHeaderDecorator());
//        mRecyclerView.setAdapter(yourAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        materialViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }
                return null;
            }
        });

        HeaderDesign.fromColorAndUrl(Color.BLUE, "http:...");
        HeaderDesign.fromColorResAndUrl(R.color.blue, "http:...");
//        HeaderDesign.fromColorAndDrawable(Color.BLUE, myDrawable);
//        HeaderDesign.fromColorResAndDrawable(R.color.blue, myDrawable);

        Toolbar toolbar = materialViewPager.getToolbar();

//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//
//            ActionBar actionBar = getSupportActionBar();
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowHomeEnabled(true);
//            actionBar.setDisplayShowTitleEnabled(true);
//            actionBar.setDisplayUseLogoEnabled(false);
//            actionBar.setHomeButtonEnabled(true);
//        }
    }

    @Override
    public void onClick(View v) {

    }
}
