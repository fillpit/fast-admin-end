package com.kenfei.admin.auth.config;

import com.kenfei.admin.auth.model.CurrentUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * spring data jpa 注入当前登陆用户
 *
 * @author fei
 * @since 2019-02-03 09:54
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.kenfei.admin.**.repository"})
public class JpaCurrentUserConfig {
  /** 注入当前登陆用户名 */
  @Bean
  public AuditorAware<String> auditorProvider() {
    return CurrentUser::userName;
  }
}
