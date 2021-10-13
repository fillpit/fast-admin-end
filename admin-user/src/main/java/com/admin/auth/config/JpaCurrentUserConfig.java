package com.admin.auth.config;

import com.admin.auth.model.CurrentUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * spring data jpa 注入当前登陆用户
 *
 * @author fei
 * @since 2019-02-03 09:54
 */
@Configuration
@EnableJpaAuditing
public class JpaCurrentUserConfig {
  /** 注入当前登陆用户名 */
  @Bean
  public AuditorAware<String> auditorProvider() {
    return CurrentUser::currentUserName;
  }
}
