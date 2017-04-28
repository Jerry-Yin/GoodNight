package com.hdu.team.hiwanan.util;

import com.hdu.team.hiwanan.listener.OnResponseListener;
import com.hdu.team.hiwanan.util.HiLog;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by JerryYin on 11/11/15.
 * 上传 音频文件至服务器的工具类
 */
public class HiUploadAudioUtil {

    private static final String TAG = "HiUploadAudioUtil";
    private static final int TIME_OUT = 10 * 1000;    //超时时间 10s
    private static final String CHARSET = "utf-8";


    /**
     * 上传音频文件的方法
     *
     * @param file       上传的文件
     * @param RequestURL 请求的url
     * @return 相应的内容
     */
    public static void upLoadAudio(final File file, final String RequestURL, final OnResponseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                String BOUNDARY = UUID.randomUUID().toString(); //边界标识   随机生成
                String CONTENT_TYPE = "multipart/form-data";   //内容类型
//                String LINE_END = "amr";    //后缀名
                String LINE_END = "\r\n";    //后缀名
                String PREFIX = "--";       //前缀

                try {
                    URL url = new URL(RequestURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(TIME_OUT);
                    connection.setReadTimeout(TIME_OUT);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);     //不允许使用缓存
                    connection.setRequestMethod("POST");
                    connection.setInstanceFollowRedirects(true);

                    /** 告诉服务器 你的客户端的配置/需求 */
                    connection.setRequestProperty("charset", CHARSET);  //设置编码
                    connection.setRequestProperty("connection", "keep-alive");
                    connection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
                    connection.connect();
                    if (file != null) {
                        /** 当文件不为空，把文件包装并且上传*/
                        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                        StringBuffer buffer = new StringBuffer();
                        buffer.append(PREFIX);
                        buffer.append(BOUNDARY);
                        buffer.append(LINE_END);
                        /**
                         * 这里重点注意：
                         * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                         * filename是文件的名字，包含后缀名的   比如:abc.png
                         */
                        buffer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + LINE_END);
                        buffer.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                        buffer.append(LINE_END);

                        outputStream.write(buffer.toString().getBytes());

                        InputStream is = new FileInputStream(file);
                        byte[] bytes = new byte[1024];
                        int len = 0;
                        if ((len = is.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, len);
                        }
                        is.close();
                        outputStream.write(LINE_END.getBytes());
                        byte[] endData = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                        outputStream.write(endData);
                        outputStream.flush();
                        /**
                         * 获取响应码  200=成功
                         * 当响应成功，获取响应的流
                         */
                        int responseCode = connection.getResponseCode();
                        HiLog.d(TAG, "responseCode = " + responseCode);
                        if (responseCode == 200) {
                            HiLog.e(TAG, "request successful");
                            InputStream inputStream = connection.getInputStream();
                            StringBuffer sbuf = new StringBuffer();
                            int ss;
                            while ((is.available() == 0) && ((ss = is.read()) != -1)) {
                                sbuf.append((char) ss);
                            }
                            result = sbuf.toString();
                            HiLog.d(TAG, "result = " + result);
                            if (listener != null) {
                                listener.onSuccess(result);
                            }
                        } else {
                            HiLog.d(TAG, "request error!!!");
                            if (listener != null) {
                                listener.onFailure(responseCode, null);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onFailure(e.hashCode(), e.getMessage());
                    }
                }
            }
        }).start();
    }

    public static void UrlConnectionPost(String url, String filePath, OnResponseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {


            }
        }).run();
    }


    public static void HttpClientPost(String url, String filePath, OnResponseListener listener) {

    }

    public static void uploadVoice(final String requestUrl, final File file, OnResponseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {

//                String boundary = "Boundary-b1ed-4060-99b9-fca7ff59c113"; // Could
                String boundary = UUID.randomUUID().toString();
                String Enter = "\r\n";

                try {
                    FileInputStream fis = new FileInputStream(file);

                    URL url = new URL(requestUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");
                    conn.setUseCaches(false);
                    conn.setInstanceFollowRedirects(true);
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                    conn.connect();

                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                    // part 1
                    String part1 = "--" + boundary + Enter + "Content-Type: application/octet-stream" + Enter
                            + "Content-Disposition: form-data; filename=\"" + file.getName() + "\"; name=\"file\"" + Enter
                            + Enter;
                    // part 2
                    String part2 = Enter + "--" + boundary + Enter + "Content-Type: text/plain" + Enter
                            + "Content-Disposition: form-data; name=\"dataFormat\"" + Enter + Enter + "hk" + Enter + "--"
                            + boundary + "--";

                    byte[] xmlBytes = new byte[fis.available()];
                    fis.read(xmlBytes);
                    dos.writeBytes(part1);
                    dos.write(xmlBytes);
                    dos.writeBytes(part2);

                    dos.flush();
                    dos.close();
                    fis.close();

                    HiLog.d("status code: " + conn.getResponseCode());

                    conn.disconnect();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
