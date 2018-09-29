package com.qf.service.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import sun.net.www.http.HttpClient;

import java.io.IOException;

public class HttpClientUtil {
    /**
     * httpClient工具 - 模拟一个页面发送请求传递json数据
     * http:
     * get传参格式:url?key=value
     * post传参格式:请求体中(url?key=value&key=value)
     *
     * 传递json数据,post应该把json数据放入到请求体中
     */
    public static String sendJsonPOST(String url,String json) {
        // 获取到HttpClient对象，用于设置请求体和设置访问路径
        // 导入apache提供的httpclient包
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
        // 创建一个post请求
        HttpPost post = new HttpPost(url);

        // 设置请求体和请求头
        // 设置为json格式
        post.setHeader(new BasicHeader("Content-Type","application/json"));
        post.setEntity(new StringEntity(json,"utf-8"));

        // 发送post请求
        CloseableHttpResponse httpResponse = httpClient.execute(post);

        // 获取请求体
            HttpEntity entity = httpResponse.getEntity();
            String entityStr = EntityUtils.toString(entity);
            return entityStr;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
