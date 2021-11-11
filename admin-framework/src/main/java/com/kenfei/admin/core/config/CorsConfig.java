package com.kenfei.admin.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 *
 * @author fei
 * @date 2021/11/8 10:41
 */
@EnableWebMvc
@Configuration(proxyBeanMethods = false)
public class CorsConfig implements WebMvcConfigurer {

  /**
   * 添加跨域响应头
   *
   * @return 跨域响应头
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedHeaders("*")
        .allowedOrigins("*")
        .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name())
        .maxAge(3600L);
  }

}
