package com.aitos.xenon.core.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientUtils {

    private static  HttpClient client = HttpClient.newHttpClient();

    public static String doGet(String url) throws IOException, InterruptedException {
        // 2.创建请求对象：request,封装请求地址和请求方式get.
        var request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        // 3.使用HttpClient对象发起request请求。得到请求响应对象response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // 4.得到响应的状态码信息
        //System.out.println(response.statusCode());
        // 5.得到响应的数据信息输出
        return response.body();
    }
}
