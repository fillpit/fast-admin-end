package com.admin.core.config.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * httpClient 配置（token 拦截器）
 * @author fei
 * @date 2018/6/24
 */
public class TokenInterceptor implements ClientHttpRequestInterceptor {
  @Override
  public ClientHttpResponse intercept(
    HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    String token = "sfasd";
    // 将令牌放入请求header中
    HttpHeaders headers = request.getHeaders();
    headers.add("X-Auth-Token", token);

    return execution.execute(request, body);
  }
}
