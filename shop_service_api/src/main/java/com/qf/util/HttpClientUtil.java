package com.qf.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        System.out.println(json);

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

    /**
     * 用于发送post请求，并且设置参数和请求头
     * 接受请求头和参数
     */
    public static String sendPostParamsAndHeader(String url,Map<String,String> params,Map<String,String> header) {
        // 获取到HttpClient对象，用于设置请求体和设置访问路径
        // 导入apache提供的httpclient包
        System.out.println("url--->" + url);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            // 创建一个post请求
            HttpPost post = new HttpPost(url);

            // 设置请求头
            if(header != null) {
                for(Map.Entry<String, String> head : header.entrySet()) {
                    System.out.println(head.getKey() + "  " + head.getValue());
                    post.addHeader(head.getKey(),head.getValue());
                }
            }

            // 设置请求体
            List<NameValuePair> paramlist = new ArrayList<>();
            if(params != null) {
                for (Map.Entry<String,String> param : params.entrySet()) {
                    paramlist.add(new BasicNameValuePair(param.getKey(),param.getValue()));
                }
            }
            post.setEntity(new UrlEncodedFormEntity(paramlist,"utf-8"));

            // 发送post请求
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            System.out.println("发送post请求完毕");

            // 获取响应体
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
