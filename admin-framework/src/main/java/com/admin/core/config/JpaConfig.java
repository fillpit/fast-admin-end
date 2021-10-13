package com.admin.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * spring data jpa 配置
 * @author fei
 * @date 2018/7/9
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.admin.**.repository"})
public class JpaConfig implements WebMvcConfigurer {

  /**
   * 注册接口参数(Pageable, Sort) 的解析器
   * 在接口中可以 @Qualifier Pageable pageable 来接收分页参数
   */
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
    argumentResolvers.add(new SortHandlerMethodArgumentResolver());
  }
}
