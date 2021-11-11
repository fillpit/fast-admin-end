package com.admin.core.config;

import com.admin.core.config.properties.AppProperties;
import com.admin.core.converter.DateConverter;
import com.admin.core.converter.EnumConverter;
import com.admin.core.resolvers.CustomerArgumentResolver;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author fei
 * @date 2017/10/12
 */
@EnableAsync
@RequiredArgsConstructor
@Configuration("coreConfig")
@EnableConfigurationProperties({AppProperties.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MvcConfig implements WebMvcConfigurer {
  Logger logger = LoggerFactory.getLogger(MvcConfig.class);

  @Value("${spring.servlet.multipart.location}")
  private String filePath;

  @Value("${app.location.source-url}")
  private String localSourceMapping;

  /**
   * 静态资源映射
   * @param registry /
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler(localSourceMapping)
      .addResourceLocations("file:" + filePath);
  }

  @Bean
  public MethodValidationPostProcessor methodValidationPostProcessor() {
    MethodValidationPostProcessor methodValidationPostProcessor =
        new MethodValidationPostProcessor();
    methodValidationPostProcessor.setValidator(validator());
    return methodValidationPostProcessor;
  }

  @Bean
  public LocaleResolver localeResolver() {
    CookieLocaleResolver slr = new CookieLocaleResolver();
    slr.setDefaultLocale(Locale.CHINA);
    slr.setCookieMaxAge(3600);
    return slr;
  }

  @Bean
  public LocalValidatorFactoryBean validator() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.setProviderClass(org.hibernate.validator.HibernateValidator.class);
    validator.setValidationMessageSource(getMessageSource());
    return validator;
  }

  private ResourceBundleMessageSource getMessageSource() {
    ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
    rbms.setDefaultEncoding(StandardCharsets.UTF_8.toString());
    rbms.setUseCodeAsDefaultMessage(false);
    rbms.setCacheSeconds(60);
    rbms.setBasenames("classpath:org/hibernate/validator/ValidationMessages");
    return rbms;
  }

  /**
   * 添加跨域响应头
   *
   * @return 跨域响应头
   */
  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod(HttpMethod.GET);
    config.addAllowedMethod(HttpMethod.POST);
    config.addAllowedMethod(HttpMethod.PUT);
    config.addAllowedMethod(HttpMethod.DELETE);
    config.addAllowedMethod(HttpMethod.OPTIONS);
    config.setMaxAge(3600L);
    source.registerCorsConfiguration("/**", config);

    return new CorsFilter(source);
  }

  /**
   * 参数转换规则.
   *
   * @param registry
   */
  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new DateConverter());
    registry.addConverterFactory(new EnumConverter());
  }

  /**
   * 修改自定义消息转换器.
   *
   * @param converters 消息转换器列表
   */
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    Jackson2ObjectMapperBuilder builder =
        new Jackson2ObjectMapperBuilder()
            .indentOutput(true)
            .locale(Locale.CHINESE)
            .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
            .timeZone(TimeZone.getTimeZone("GMT+8"))
            .modulesToInstall(new ParameterNamesModule());
    converters.add(0, new MappingJackson2HttpMessageConverter(builder.build()));
    //    converters.add(1, new
    // MappingJackson2XmlHttpMessageConverter(builder.createXmlMapper(true).build()));
  }

  /**
   * 配置支持返回不同的内容类型.
   *
   * @param configurer
   */
  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer
        .favorPathExtension(false)
        .favorParameter(true)
        .parameterName("mediaType")
        .defaultContentType(MediaType.APPLICATION_JSON)
        .mediaType("xml", MediaType.APPLICATION_XML)
        .mediaType("html", MediaType.TEXT_HTML)
        .mediaType("json", MediaType.APPLICATION_JSON);
  }

  /**
   * 1、 extends WebMvcConfigurationSupport
   *
   * <p>2、重写下面方法;
   *
   * <p>setUseSuffixPatternMatch : 设置是否是后缀模式匹配，如“/user”是否匹配/user.*，default: true；
   *
   * <p>setUseTrailingSlashMatch : 设置是否自动后缀路径模式匹配，如“/user”是否匹配“/user/”，default: true；
   */
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.setUseSuffixPatternMatch(true).setUseTrailingSlashMatch(false);
  }

  /**
   * 添加参数装载.
   *
   * @param argumentResolvers
   */
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    // 将自定义的参数装载添加到spring内托管
    argumentResolvers.add(new CustomerArgumentResolver());
  }
}
