package com.kenfei.admin.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.nio.charset.StandardCharsets;

/**
 * 参数验证配置
 * @author fei
 * @date 2021/11/8 10:43
 */
@Configuration
public class ValidatorConfig {

  @Bean
  public MethodValidationPostProcessor methodValidationPostProcessor() {
    MethodValidationPostProcessor methodValidationPostProcessor =
        new MethodValidationPostProcessor();
    methodValidationPostProcessor.setValidator(validator());
    return methodValidationPostProcessor;
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
}
