package com.kenfei.admin.core.config;

import com.kenfei.admin.core.common.UploadProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * bean 配置
 * @author fei
 * @date 2021/11/22 12:05
 */
@Configuration(proxyBeanMethods = false)
public class BeanConfig {

  /**
   * 文件上传的相关配置
   * @return /
   */
  @Bean
  public UploadProperties getUploadProperties() {
    return new UploadProperties();
  }
}

