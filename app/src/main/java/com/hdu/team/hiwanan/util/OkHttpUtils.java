package com.hdu.team.hiwanan.util;

import android.os.Looper;
import android.util.Log;

import com.hdu.team.hiwanan.constant.HiConfig;
import com.hdu.team.hiwanan.listener.OnResponseListener;
import com.hdu.team.hiwanan.model.HiMaps;
import com.hdu.team.hiwanan.model.HiReqSleepTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
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
                                listener.onFailure(call.hashCode(), response.body().toString() + " " + str);
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
//                if (paramsList != null) {
//                    for (HiMaps map : paramsList) {
//                        HiLog.d(TAG, map.getValue());
//                        try {
//                            builder.add(map.getKey(), new String(map.getValue().getBytes("utf-8"), "utf-8"));
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }

                builder.add("action", "query");
                builder.add("params", "{\"userId\":2, \"duration\":7}");
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
                                listener.onFailure(call.hashCode(), response.body().toString() + " " + str);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * Post 方式, 但以参数
     *
     * @param url
     * @param    (请求参数的 key / value)
     * @param listener
     */
//    public static void OkHttpPost(final String url, final HiReqSleepTime params, final OnResponseListener listener) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "url---" + url);
//                OkHttpClient okHttpClient = new OkHttpClient();
//
//                FormBody.Builder builder = new FormBody.Builder();
//                builder.add(HiConfig.REQ_ACTION, params.getAction());
//
//                HiReqSleepTime.ParamsBean bean = params.getParams();
//                builder.add(HiConfig.REQ_PARAMS, "{"+);
//
//                RequestBody requestBody = builder.build();
//
//                Request request = new Request.Builder()
//                        .url(url)
//                        .post(requestBody)
//                        .build();
//
//                Call call = okHttpClient.newCall(request);
//                call.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        if (listener != null) {
//                            listener.onFailure(call.hashCode(), e.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.d(TAG, "response----" + response.body() + "/ " + response.message() + "/ " + response.cacheResponse() + "/ " + response.code() + "/ ");
//                        String result = response.body().string();
//                        if (result.toString() != null) {
//                            if (listener == null) return;
////                    String result = response.cacheResponse().toString();
//                            listener.onSuccess(result);
//                            Log.i(TAG, "cache---" + result);
//                        } else {
//                            String str = response.networkResponse().toString();
//                            Log.i(TAG, "network---" + str);
//                            if (listener != null) {
//                                listener.onFailure(call.hashCode(), response.body().toString()+" "+str);
//                            }
//                        }
//                    }
//                });
//            }
//        }).start();
//
//    }


    /**
     * @param url
     * @param param
     * @param listener
     * @return
     */
    public static void sendPost(final String url, final String param, final OnResponseListener<String> listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter out = null;
                BufferedReader in = null;
                String result = "";
                try {
                    URL realUrl = new URL(url);
                    // 打开和URL之间的连接
                    URLConnection conn = realUrl.openConnection();
                    // 设置通用的请求属性
                    conn.setRequestProperty("accept", "*/*");
                    conn.setRequestProperty("connection", "Keep-Alive");
                    conn.setRequestProperty("user-agent",
                            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                    // 发送POST请求必须设置如下两行
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    // 获取URLConnection对象对应的输出流
                    out = new PrintWriter(conn.getOutputStream());
                    // 发送请求参数
                    out.print(param);
                    // flush输出流的缓冲
                    out.flush();
                    // 定义BufferedReader输入流来读取URL的响应
                    in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    if (!result.isEmpty()) {
                        listener.onSuccess(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(e.hashCode(), e.getMessage());
                }
                //使用finally块来关闭输出流、输入流
                finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        listener.onFailure(ex.hashCode(), ex.getMessage());
                    }
                }
            }
        }).start();

    }
}
