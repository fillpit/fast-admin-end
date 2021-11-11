package com.kenfei.admin.core.config;

import com.kenfei.admin.core.config.filter.PageParamsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * @author fei
 * @date 2021/11/9 16:15
 */
@Configuration(proxyBeanMethods = false)
public class FilterConfig {

  /**
   * 分页参数处理过滤器
   * @return /
   */
  @Bean
  public FilterRegistrationBean<PageParamsFilter> registFilter() {
    FilterRegistrationBean<PageParamsFilter> registration =
        new FilterRegistrationBean<>(new PageParamsFilter());
    registration.addUrlPatterns("/*");
    registration.setName("ParamsFilter");
    registration.setOrder(1);
    registration.setDispatcherTypes(DispatcherType.REQUEST);
    return registration;
  }
}
