package com.kenfei.admin.core.config;

import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.kenfei.admin.core.common.AppProperties;
import com.kenfei.admin.core.common.UploadProperties;
import com.kenfei.admin.core.base.BaseEnum;
import com.kenfei.admin.core.exception.DateConverterException;
import com.kenfei.admin.core.config.resolvers.CustomerArgumentResolver;
import com.kenfei.admin.core.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.StringUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fei
 * @date 2017/10/12
 */
@EnableAsync
@Configuration(value = "coreConfig", proxyBeanMethods = false)
@EnableConfigurationProperties({AppProperties.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MvcConfig implements WebMvcConfigurer {
  Logger logger = LoggerFactory.getLogger(MvcConfig.class);

  private final UploadProperties uploadProperties;

  @Autowired
  public MvcConfig(UploadProperties uploadProperties) {
    this.uploadProperties = uploadProperties;
  }

  @Bean
  public SpringContextUtils springContextUtils(){
    return new SpringContextUtils();
  }


  /**
   * ??????????????????
   * @param registry /
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler(uploadProperties.getMappingPath())
      .addResourceLocations("file:" + uploadProperties.getSavePath());
  }

  @Bean
  public LocaleResolver localeResolver() {
    CookieLocaleResolver slr = new CookieLocaleResolver();
    slr.setDefaultLocale(Locale.CHINA);
    slr.setCookieMaxAge(3600);
    return slr;
  }

  /**
   * ??????????????????.
   *
   * @param registry
   */
  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new DateConverter());
    registry.addConverterFactory(new EnumConverter());
  }

  /**
   * ??????????????????????????????.
   *
   * @param converters ?????????????????????
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
   * ???????????????????????????????????????.
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
   * 1??? extends WebMvcConfigurationSupport
   *
   * <p>2?????????????????????;
   *
   * <p>setUseSuffixPatternMatch : ??????????????????????????????????????????/user???????????????/user.*???default: true???
   *
   * <p>setUseTrailingSlashMatch : ???????????????????????????????????????????????????/user??????????????????/user/??????default: true???
   */
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.setUseSuffixPatternMatch(true).setUseTrailingSlashMatch(false);
  }

  /**
   * ??????????????????.
   *
   * @param argumentResolvers
   */
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    // ????????????????????????????????????spring?????????
    argumentResolvers.add(new CustomerArgumentResolver());
  }

  /**
   * ????????????????????????String -> Date???.
   *
   * @author fei
   * @date 2018/2/27
   */
  public class DateConverter implements Converter<String, Date> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date convert(String s) {
      if (!StringUtils.hasLength(s)) {
        return null;
      }

      // ???????????????
      String[] v = s.trim().split(" ");
      if (v.length == 1) {
        s = s.concat(" 00:00:00");
      }

      try {
        return dateFormat.parse(s);
      } catch (ParseException e) {
        throw new DateConverterException("[" + s + "]??????????????????, ???????????? yyyy-MM-dd HH:mm:ss");
      }
    }
  }

  /**
   * ?????????????????????
   * @author fei
   * @date 2018/6/14
   */
  public static class EnumConverter implements ConverterFactory<String, BaseEnum> {

    private static final Map<Class, Converter> CONVERTER_MAP = new WeakHashMap<>();

    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> aClass) {
      Converter result = CONVERTER_MAP.get(aClass);
      if(result == null) {
        result = new StrToEnum<>(aClass);
        CONVERTER_MAP.put(aClass, result);
      }
      return result;
    }

    class StrToEnum<T extends BaseEnum> implements Converter<String, T> {

      private final Class<T> enumType;
      private Map<String, T> enumMap = new HashMap<>();

      public StrToEnum(Class<T> enumType) {
        this.enumType = enumType;
        T[] enums = enumType.getEnumConstants();
        for(T e : enums) {
          enumMap.put(e.value() + "", e);
        }
      }

      @Override
      public T convert(String s) {
        T result = enumMap.get(s);
        if(result == null) {
          throw new IllegalArgumentException("No element matches [" + s + "]");
        }
        return result;
      }
    }
  }

}
