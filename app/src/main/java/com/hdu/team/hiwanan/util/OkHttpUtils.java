package com.hdu.team.hiwanan.util;

import android.util.Log;

import com.hdu.team.hiwanan.listener.OnResponseListener;
import com.hdu.team.hiwanan.model.HiMaps;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by JerryYin on 10/14/16.
 */

public class OkHttpUtils {

    public static final String TAG = "OkHttpUtils";



    /**
     * Get 方式
     *
     * @param url
     * @param listener
     */
    public static void OkHttpGet(final String url, final OnResponseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "url---" + url);
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder requestBuilder = new Request.Builder().url(url);
//                requestBuilder.method("GET", null);     //可省略
                final Request request = requestBuilder.build();
                Call call = okHttpClient.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i(TAG, "exception---" + e.getMessage());
                        if (listener != null) {
                            listener.onFailure(e.hashCode(), e.getMessage().toString());
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d(TAG, "response----" + response.body() + "/ " + response.message() + "/ " + response.cacheResponse() + "/ " + response.code() + "/ ");
                        if (response.body() != null) {
                            if (listener == null) return;
                            String result = response.body().string();
                            listener.onSuccess(result);
                            Log.i(TAG, "cache---" + result);
                        } else {
                            String str = response.networkResponse().toString();
                            Log.i(TAG, "network---" + str);
                            if (listener != null) {
                                listener.onFailure(call.hashCode(), response.body().toString()+" "+str);
                            }
                        }
                    }
                });
            }
        }).start();


//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                if (listener != null) {
//                    listener.onFailure(call.hashCode(), e.getMessage());
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d(TAG, "response----"+response.body().toString()+"/ "+response.message()+"/ "+response.cacheResponse()+"/ "+response.code()+"/ ");
//                if (response.cacheResponse() != null) {
//                    if (listener == null) return;
//                    String result = response.cacheResponse().toString();
//                    listener.onSuccess(result);
//                    Log.i(TAG, "cache---" + result);
//                } else {
//                    response.body().string();
//                    String str = response.networkResponse().toString();
//                    Log.i(TAG, "network---" + str);
//                }
//            }
//        });
    }


    /**
     * Post 方式, 但以参数
     *
     * @param url
     * @param paramsList (请求参数的  key / value)
     * @param listener
     */
    public static void OkHttpPost(final String url, final List<HiMaps> paramsList, final OnResponseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "url---" + url);
                OkHttpClient okHttpClient = new OkHttpClient();

                FormBody.Builder builder = new FormBody.Builder();
                if (paramsList != null) {
                    for (HiMaps map : paramsList) {
                        builder.add(map.getKey(), map.getValue());
                    }
                }
                RequestBody requestBody = builder.build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (listener != null) {
                            listener.onFailure(call.hashCode(), e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d(TAG, "response----" + response.body() + "/ " + response.message() + "/ " + response.cacheResponse() + "/ " + response.code() + "/ ");
                        String result = response.body().string();
                        if (result.toString() != null) {
                            if (listener == null) return;
//                    String result = response.cacheResponse().toString();
                            listener.onSuccess(result);
                            Log.i(TAG, "cache---" + result);
                        } else {
                            String str = response.networkResponse().toString();
                            Log.i(TAG, "network---" + str);
                            if (listener != null) {
                                listener.onFailure(call.hashCode(), response.body().toString()+" "+str);
                            }
                        }
                    }
                });
            }
        }).start();

    }



}
