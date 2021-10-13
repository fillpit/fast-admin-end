package com.admin.core.config;

import com.admin.core.config.http.CustomResponseErrorHandler;
import com.admin.core.config.http.HeaderInterceptor;
import com.admin.core.config.http.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * httpClient 配置
 * @author fei
 * @date 2018/6/24
 */
@Configuration
public class HttpClientConfig {

  @Bean
  public RestTemplate getRestTemplate() {
    // ClientHttpRequestFactory作为参数构造一个使用作为底层的RestTemplate
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setReadTimeout(5000);
    factory.setConnectTimeout(5000);

    RestTemplate restTemplate = new RestTemplate(factory);

    // 添加请求拦截器
    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
    interceptors.add(new TokenInterceptor());
    interceptors.add(new HeaderInterceptor());
    // 设置拦截器
    restTemplate.setInterceptors(interceptors);
    // 设置统一错误处理
    restTemplate.setErrorHandler(new CustomResponseErrorHandler());

    return restTemplate;
  }
}
