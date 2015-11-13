package com.hdu.team.hiwanan.utils;

import com.hdu.team.hiwanan.util.HiLog;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by JerryYin on 11/11/15.
 * 上传 音频文件至服务器的工具类
 */
public class HiUploadAudioUtil {

    private static final String TAG = "HiUploadAudioUtil";
    private static final int TIME_OUT = 10*1000;    //超时时间 10s
    private static final String CHARSET = "utf-8" ;


    /**
     * 上传音频文件的方法
     * @param file 上传的文件
     * @param RequestURL 请求的url
     * @return  相应的内容
     */
    public static String upLoadAudio(File file, String RequestURL){
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); //边界标识   随机生成
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        String LINE_END = "amr";    //后缀名
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

            /** 告诉服务器 你的客户端的配置/需求 */
            connection.setRequestProperty("charset", CHARSET);  //设置编码
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("Content-Type",  CONTENT_TYPE + ";boundary=" + BOUNDARY);

            if (file != null){
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
                buffer.append("Content-Disposition: form-data; name=\"audio\"; filename=\""+file.getName()+"\""+LINE_END);
                buffer.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
                buffer.append(LINE_END);

                outputStream.write(buffer.toString().getBytes());

                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                if ((len = is.read(bytes)) != -1){
                    outputStream.write(bytes, 0, len);
                }
                is.close();
                outputStream.write(LINE_END.getBytes());
                byte[] endData = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                outputStream.write(endData);
                outputStream.flush();
                /**
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流
                 */
                int responseCode = connection.getResponseCode();
                HiLog.e(TAG, "responseCode = "+responseCode);
                if (responseCode == 200){
                    HiLog.e(TAG, "request successful");
                    InputStream inputStream = connection.getInputStream();
                    StringBuffer sbuf = new StringBuffer();
                    int ss;
                    while ((ss = is.read()) != -1){
                        sbuf.append((char)ss);
                    }
                    result = sbuf.toString();
                    HiLog.e(TAG, "result = "+result);
                }else {
                    HiLog.e(TAG, "request error!!!");
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
