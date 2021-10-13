package com.admin.auth.config;

import com.admin.auth.config.properties.TokenProperties;
import com.admin.core.config.properties.AppProperties;
import com.admin.core.config.properties.UploadProperties;
import com.admin.core.repository.Response;
import com.admin.auth.config.security.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * spring security 配置
 * @author fei
 * @date 2018/10/21
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({TokenProperties.class})
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired private JwtUserDetailsServiceImpl userDetailsService;
  @Autowired private CustomMetadataSource metadataSource;
  @Autowired private UrlAccessDecisionManager urlAccessDecisionManager;
  @Autowired private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

  @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
  }

  /**
   * 主要配置忽略的静态资源路径
   *
   * @param web web 安全对象
   */
  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
        .antMatchers(HttpMethod.GET, "/index.html", "/static/**")
        .mvcMatchers("/api/auth/**")
        .mvcMatchers("/upload/**");
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    // 关于授权
    httpSecurity
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        // 就是鉴权拉
        .withObjectPostProcessor(this.objectPostProcessor())
        .and()
        .logout()
        .logoutUrl("/auth/api/logout")
        .permitAll()
        // 其它设置
        .and()
        .csrf()
        .disable()
        // 关于跨域, 这表示允许跨域，加上 disable 表示不允许，流下无法理解的眼泪
        .cors()
        .and()
        // 关于异常处理器
        .exceptionHandling()
        .authenticationEntryPoint(this.getAuthenticationEntryPoint())
        .and()
        // 关于session 会话
        .sessionManagement()
        // 用token实现会话当然也不需要这东西拉
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
  }


  /** 资源访问信息初始化 */
  ObjectPostProcessor objectPostProcessor() {
    return new ObjectPostProcessor<FilterSecurityInterceptor>() {
      @Override
      public <O extends FilterSecurityInterceptor> O postProcess(O o) {
        o.setSecurityMetadataSource(metadataSource);
        o.setAccessDecisionManager(urlAccessDecisionManager);
        return o;
      }
    };
  }

  private AuthenticationEntryPoint getAuthenticationEntryPoint() {
    return (httpServletRequest, httpServletResponse, e) -> {
      httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      httpServletResponse.setContentType("application/json;charset=UTF-8");
      PrintWriter out = httpServletResponse.getWriter();
      Response error = Response.builder().success(false).message("用户没有初始化").build();
      out.write(new ObjectMapper().writeValueAsString(error));
      out.flush();
      out.close();
    };
  }

}

