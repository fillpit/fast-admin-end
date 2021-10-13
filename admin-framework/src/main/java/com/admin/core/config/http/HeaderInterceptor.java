package com.admin.core.config.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fei
 * @date 2018/6/24
 */
public class HeaderInterceptor implements ClientHttpRequestInterceptor {
  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

    // 获取头容器
    HttpHeaders headers = request.getHeaders();

    // 设置期望内容格式
    List<MediaType> accepts = new ArrayList<>();
    accepts.add(MediaType.APPLICATION_JSON);
    headers.setAccept(accepts);

    // 设置请求内容格式
    headers.add("Content-type", "application/json");

    return execution.execute(request, body);
  }
}
