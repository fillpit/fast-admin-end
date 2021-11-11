package com.kenfei.admin.core.config;

import com.kenfei.admin.core.log.WebLogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fei
 * @date 2021/11/9 16:41
 */
@Configuration(proxyBeanMethods = false)
public class AspectConfig {

  @Bean
  public WebLogAspect webLogAspect() {
    return new WebLogAspect();
  }
}
