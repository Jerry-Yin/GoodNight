//package com.hdu.team.hiwanan.util;
//
///**
// * Created by JerryYin on 4/27/17.
// */
//import org.apache.http.*;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.FileEntity;
//import org.apache.http.entity.StringEntity;
////import org.apache.http.impl.client.CloseableHttpClient;
////import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
////import org.apache.log4j.Logger;
//import java.io.*;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//import java.util.logging.Logger;
//
///**
// * 常用工具类：Apache HttpClient工具
// *
// * @author Jie
// * @date 2015-2-12
// * @since JDK1.6
// */
//public class HttpClientHelper {
//
//    private static Logger log = Logger.getLogger(String.valueOf(HttpClientHelper.class));
//
//    private static List<String> mineTypeList = new ArrayList<String>();
//
//    static {
//        mineTypeList.add("application/octet-stream");
//        mineTypeList.add("application/pdf");
//        mineTypeList.add("application/msword");
//
//        mineTypeList.add("image/png");
//        mineTypeList.add("image/jpg");
//        mineTypeList.add("image/jpeg");
//        mineTypeList.add("image/gif");
//        mineTypeList.add("image/bmp");
//
//        mineTypeList.add("audio/amr");
//        mineTypeList.add("audio/mp3");
//        mineTypeList.add("audio/aac");
//        mineTypeList.add("audio/wma");
//        mineTypeList.add("audio/wav");
//
//        mineTypeList.add("video/mpeg");
//    }
//
//    /***
//     * HttpClient GET请求，Header参数
//     *
//     * @param uri 请求地址
//     * @param name 参数名称
//     * @param value 参数值
//     * @return 响应字符串
//     * @author Jie
//     * @date 2015年7月7日
//     */
//    public static String getMethod(String uri, String name, String value) throws IOException {
//        log.info("------------------------------HttpClient GET BEGIN-------------------------------");
//        log.info("GET:" + uri);
//        if (uri.isEmpty()) {
//            throw new RuntimeException(" uri parameter is null or is empty!");
//        }
//        log.info("req:[" + name + "=" + value + "]");
//        CloseableHttpClient httpClient = null;
//        HttpGet httpGet = null;
//        String respContent = null;
//        try {
//            // 创建GET请求
//            httpClient = HttpClients.createDefault();
//            httpGet = new HttpGet(uri);
//            httpGet.addHeader(name, value);
//            // 提交GET请求
//            HttpResponse response = httpClient.execute(httpGet);
//            // 获取响应内容
//            respContent = repsonse(response);
//            if (respContent.startsWith("code")) {
//                log.info("resp：" + respContent);
//                throw new RuntimeException("请求失败，请检查URL地址和请求参数...");
//            }
//        } finally {
//            if (httpGet != null) {
//                httpGet.releaseConnection();
//            }
//            if (httpClient != null) {
//                httpClient.close();
//            }
//        }
//        log.info("resp：" + respContent);
//        log.info("------------------------------HttpClient GET END-------------------------------");
//        return respContent;
//    }
//
//    /**
//     * HttpClient GET请求，可接受普通文本JSON等
//     *
//     * @param uri Y 请求URL，参数封装
//     * @return 响应字符串
//     * @author Jie
//     * @date 2015-2-12
//     */
//    public static String getMethod(String uri) throws IOException {
//        log.info("------------------------------HttpClient GET BEGIN-------------------------------");
//        log.info("GET:" + uri);
//        if (StringUtils.isBlank(uri)) {
//            throw new RuntimeException(" uri parameter is null or is empty!");
//        }
//        // 创建GET请求
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpGet = null;
//        String respContent = "";
//        try {
//            httpGet = new HttpGet(uri);
//            HttpResponse response = httpClient.execute(httpGet);
//            StatusLine statusLine = response.getStatusLine();
//            int statusCode = statusLine.getStatusCode();// 响应码
//            String reasonPhrase = statusLine.getReasonPhrase();// 响应信息
//            if (statusCode == 200) {// 请求成功
//                // 获取响应MineType
//                HttpEntity entity = response.getEntity();
//                ContentType contentType = ContentType.get(entity);
//                if (mineTypeList.contains(contentType.getMimeType().toLowerCase())) {// 下载失败
//                    log.info("MineType:" + contentType.getMimeType());
//                } else {
//                    respContent = repsonse(response);
//                }
//            } else {
//                log.error("resp：code[" + statusCode + "],desc[" + reasonPhrase + "]");
//                throw new RuntimeException("请求失败，请检查请求地址及参数");
//            }
//        } finally {
//            if (httpGet != null)
//                httpGet.releaseConnection();
//            if (httpClient != null)
//                // noinspection ThrowFromFinallyBlock
//                httpClient.close();
//        }
//        log.info("resp：" + respContent);
//        log.info("------------------------------HttpClient GET END-------------------------------");
//        return respContent;
//    }
//
//    /**
//     * HttpClient GET请求，可接受普通文本JSON等
//     *
//     * @param uri Y 请求URL，参数封装
//     * @return 响应字符串
//     * @author Jie
//     * @date 2015-2-12
//     */
//    public static String getForDownloadStream(String uri, String targetPath) throws IOException {
//        log.info("------------------------------HttpClient GET BEGIN-------------------------------");
//        log.info("GET:" + uri);
//        if (StringUtils.isBlank(uri) || StringUtils.isBlank(targetPath)) {
//            throw new RuntimeException(" uri or targetPath parameter is null or is empty!");
//        }
//        // 创建GET请求
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpGet = null;
//        String respContent = "";
//        try {
//            httpGet = new HttpGet(uri);
//            HttpResponse response = httpClient.execute(httpGet);
//            StatusLine statusLine = response.getStatusLine();
//            int statusCode = statusLine.getStatusCode();// 响应码
//            String reasonPhrase = statusLine.getReasonPhrase();// 响应信息
//            if (statusCode == 200) {// 请求成功
//                // 获取响应MineType
//                HttpEntity entity = response.getEntity();
//                ContentType contentType = ContentType.get(entity);
//                if (mineTypeList.contains(contentType.getMimeType().toLowerCase())) {
//                    log.info("MineType:" + contentType.getMimeType());
//
//                    if (targetPath.contains(".")) {
//                        targetPath = targetPath.substring(0, targetPath.lastIndexOf(".")) + "."
//                                + contentType.getMimeType().split("/")[1];
//                    } else if (targetPath.endsWith(File.separator)) {
//                        targetPath += UUID.randomUUID().toString() + "." + contentType.getMimeType().split("/")[1];
//                    } else {
//                        targetPath += File.separator + UUID.randomUUID().toString() + "."
//                                + contentType.getMimeType().split("/")[1];
//                    }
//                    // 写入磁盘
//                    respContent = FileHelper.writeFile(entity.getContent(), targetPath);
//                } else {
//                    respContent = repsonse(response);
//                }
//            } else {
//                log.error("resp：code[" + statusCode + "],desc[" + reasonPhrase + "]");
//                throw new RuntimeException("请求失败，请检查请求地址及参数");
//            }
//        } finally {
//            if (httpGet != null)
//                httpGet.releaseConnection();
//            if (httpClient != null)
//                // noinspection ThrowFromFinallyBlock
//                httpClient.close();
//        }
//        log.info("resp：" + respContent);
//        log.info("------------------------------HttpClient GET END-------------------------------");
//        return respContent;
//    }
//
//    /**
//     * HttpClient POST请求 ,传参方式：key-value
//     *
//     * @param uri 请求地址
//     * @param params 参数列表
//     * @return 响应字符串
//     * @author Jie
//     * @date 2015-2-12
//     */
//    @SuppressWarnings("ThrowFromFinallyBlock")
//    public static String postMethod(String uri, List<NameValuePair> params) throws IOException {
//        log.info("------------------------------HttpClient POST BEGIN-------------------------------");
//        log.info("POST:" + uri);
//        if (StringUtils.isBlank(uri)) {
//            throw new RuntimeException(" uri parameter is null or is empty!");
//        }
//        log.info("req:" + params);
//        // 创建GET请求
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpPost httpPost = null;
//        String respContent = null;
//        try {
//            httpPost = new HttpPost(uri);
//            httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
//            // 执行请求
//            HttpResponse response = httpClient.execute(httpPost);
//            // 获取响应内容
//            respContent = repsonse(response);
//            if (respContent.startsWith("code")) {
//                log.info("resp：" + respContent);
//                throw new RuntimeException("请求失败，请检查URL地址和请求参数...");
//            }
//        } finally {
//            close(null, null, null, httpPost, httpClient);
//        }
//        log.info("resp：" + respContent);
//        log.info("------------------------------HttpClient POST END-------------------------------");
//        return respContent;
//    }
//
//    /**
//     * HttpClient POST请求 ,上传多媒体文件
//     *
//     * @param url 请求地址
//     * @param filePath 多媒体文件绝对路径
//     * @return 多媒体文件ID
//     * @throws UnsupportedEncodingException
//     * @author Jie
//     * @date 2015-2-12
//     */
//    @SuppressWarnings("resource")
//    public static String postForUploadStream(String url, String filePath) throws IOException {
//        log.info("------------------------------HttpClient POST开始-------------------------------");
//        log.info("POST:" + url);
//        log.info("filePath:" + filePath);
//        if (url.isEmpty()) {
//            log.warning("post请求不合法，请检查uri参数!");
//            return null;
//        }
//        StringBuilder content = new StringBuilder();
//
//        // 模拟表单上传 POST 提交主体内容
//        String boundary = "-----------------------------" + new Date().getTime();
//        // 待上传的文件
//        File file = new File(filePath);
//
//        if (!file.exists() || file.isDirectory()) {
//            log.warning(filePath + ":不是一个有效的文件路径");
//            return null;
//        }
//
//        // 响应内容
//        String respContent = null;
//
//        InputStream is = null;
//        OutputStream os = null;
//        BufferedInputStream bis = null;
//        File tempFile = null;
//        HttpClient httpClient = null;
//        HttpPost httpPost = null;
//        try {
//            // 创建临时文件，将post内容保存到该临时文件下，临时文件保存在系统默认临时目录下，使用系统默认文件名称
//            tempFile = File.createTempFile(new SimpleDateFormat("yyyy_MM_dd").format(new Date()), null);
//            os = new FileOutputStream(tempFile);
//            is = new FileInputStream(file);
//
//            os.write(("--" + boundary + "\r\n").getBytes());
//            os.write(String.format(
//                    "Content-Disposition: form-data; name=\"media\"; filename=\"" + file.getName() + "\"\r\n")
//                    .getBytes());
//            os.write(String.format("Content-Type: %s\r\n\r\n", FileHelper.getMimeType(file)).getBytes());
//
//            // 读取上传文件
//            bis = new BufferedInputStream(is);
//            byte[] buff = new byte[8096];
//            int len = 0;
//            while ((len = bis.read(buff)) != -1) {
//                os.write(buff, 0, len);
//            }
//
//            os.write(("\r\n--" + boundary + "--\r\n").getBytes());
//
//            httpClient = HttpClients.createDefault();
//            // 创建POST请求
//            httpPost = new HttpPost(url);
//
//            // 创建请求实体
//            FileEntity reqEntity = new FileEntity(tempFile, ContentType.MULTIPART_FORM_DATA);
//
//            // 设置请求编码
//            reqEntity.setContentEncoding("UTF-8");
//            httpPost.setEntity(reqEntity);
//            // 执行请求
//            HttpResponse response = httpClient.execute(httpPost);
//            // 获取响应内容
//            respContent = repsonse(response);
//            if(respContent.startsWith("code")) {
//                log.info("resp：" + respContent);
//                throw new RuntimeException("请求失败，请检查URL地址和请求参数...");
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (bis != null) {
//                bis.close();
//            }
//
//            if (is != null) {
//                is.close();
//            }
//
//            if (os != null) {
//                os.close();
//            }
//
//            if (httpPost != null) {
//                httpPost.releaseConnection();
//            }
//
//            if (httpClient != null) {
//                httpClient.close();
//            }
//        }
//        log.info("resp：" + respContent);
//        log.info("------------------------------HttpClient POST结束-------------------------------");
//        return respContent;
//    }
//
//    /**
//     * 获取响应内容，针对MimeType为text/plan、text/json格式
//     *
//     * @param response HttpResponse对象
//     * @return 转为UTF-8的字符串
//     * @author Jie
//     * @date 2015-2-28
//     */
//    private static String repsonse(HttpResponse response) throws ParseException, IOException {
//        StatusLine statusLine = response.getStatusLine();
//        int statusCode = statusLine.getStatusCode();// 响应码
//        String reasonPhrase = statusLine.getReasonPhrase();// 响应信息
//        StringBuilder content = new StringBuilder();
//        if (statusCode == HttpStatus.SC_OK) {// 请求成功
//            HttpEntity entity = response.getEntity();
//            ContentType contentType = ContentType.get(entity);
//            log.info("MineType：" + contentType.getMimeType());
//            content.append(EntityUtils.toString(entity, Consts.UTF_8));
//        } else {
//            content.append("code[").append(statusCode).append("],desc[").append(reasonPhrase).append("]");
//        }
//        return content.toString().replace("\r\n", "").replace("\n", "");
//    }
//
//    // 释放资源
//    private static void close(File tempFile, OutputStream os, InputStream is, HttpPost httpPost,
//                              CloseableHttpClient httpClient) throws IOException {
//        if (tempFile != null && tempFile.exists() && !tempFile.delete()) {
//            tempFile.deleteOnExit();
//        }
//        if (os != null) {
//            os.close();
//        }
//        if (is != null) {
//            is.close();
//        }
//        if (httpPost != null) {
//            // 释放资源
//            httpPost.releaseConnection();
//        }
//        if (httpClient != null) {
//            httpClient.close();
//        }
//    }
//
//    /**
//     * HttpClient POST请求 ,可接受普通字符响应，也可支持下载多媒体文件
//     *
//     * @param uri Y 请求地址
//     * @param params Y 请求参数串，推荐使用JSON格式
//     * @return 响应字符串
//     * @author Jie
//     * @date 2016年4月8日
//     */
//    public static String postMethod(String uri, String params) throws IOException {
//        log.info("------------------------------HttpClient POST BEGIN-------------------------------");
//        log.info("uri:" + uri);
//        if (StringUtils.isBlank(uri)) {
//            throw new RuntimeException(" uri parameter is null or is empty!");
//        }
//        // 响应内容
//        InputStream is = null;
//        CloseableHttpClient httpClient = null;
//        HttpPost httpPost = null;
//        String respContent = "";
//        try {
//
//            httpClient = HttpClients.createDefault();
//            // 创建POST请求
//            httpPost = new HttpPost(uri);
//            httpPost.setEntity(new StringEntity(params, Consts.UTF_8));
//            // 执行请求
//            HttpResponse response = httpClient.execute(httpPost);
//            // 获取响应信息
//            StatusLine statusLine = response.getStatusLine();
//            int statusCode = statusLine.getStatusCode();
//            String reasonPhrase = statusLine.getReasonPhrase();// 响应信息
//            if (statusCode == HttpStatus.SC_OK) {// 请求成功
//                HttpEntity entity = response.getEntity();
//                ContentType contentType = ContentType.get(entity);
//                if (mineTypeList.contains(contentType.getMimeType().toLowerCase())) {
//                    log.info("MineType ：" + contentType.getMimeType());
//                    respContent = StreamHelper.read(entity.getContent());
//                } else {
//                    // 获取响应内容
//                    respContent = repsonse(response);
//                    if (respContent.startsWith("code")) {
//                        log.info("resp：" + respContent);
//                        throw new RuntimeException("请求失败，请检查URL地址和请求参数...");
//                    }
//                }
//            } else {
//                log.error("code[" + statusCode + "],desc[" + reasonPhrase + "]");
//                throw new RuntimeException("请求失败，请检查请求地址或请求参数");
//            }
//        } finally {
//            // noinspection ThrowFromFinallyBlock
//            close(null, null, is, httpPost, httpClient);
//        }
//        log.info("resp：" + respContent);
//        log.info("------------------------------HttpClient POST END-------------------------------");
//        return respContent;
//    }
//
//    public static void main(String[] args) {
//        String s = String.format("asdlkfajfk%naskdfjdlksaf");
//        System.out.println(s);
//    }
//}
