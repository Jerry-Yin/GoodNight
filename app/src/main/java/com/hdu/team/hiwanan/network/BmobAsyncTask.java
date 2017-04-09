package com.hdu.team.hiwanan.network;

import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.hdu.team.hiwanan.model.bmob.UserBmob;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by JerryYin on 4/9/17.
 */

public class BmobAsyncTask<T extends BmobObject> extends AsyncTask<String, Integer, T> {


    private BmobQuery<T> mQuery;
    private T mResult;
    private ProgressBar mProgressBar;

    public BmobAsyncTask(ProgressBar bar) {
        if (mQuery == null)
            mQuery = new BmobQuery<T>();
        this.mProgressBar = bar;
    }

    @Override
    protected T doInBackground(String... params) {
        mQuery.getObject(String.valueOf(params), new QueryListener<T>() {
            @Override
            public void done(T t, BmobException e) {
                if (e == null)
                    return;
                mResult = t;
            }
        });
        return mResult;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mProgressBar != null) {
            mProgressBar.setProgress(values[0]);
        }
    }

    @Override
    protected void onPostExecute(T bmobObject) {
        super.onPostExecute(bmobObject);

    }


}
