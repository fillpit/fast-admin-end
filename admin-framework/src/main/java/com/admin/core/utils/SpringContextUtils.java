package com.admin.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring context 工具类
 * @author fei
 * @since 2019/12/20 16:29
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
  private static ApplicationContext applicationContext = null;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    System.out.println("applicationContext正在初始化,application:"+applicationContext);
    if (SpringContextUtils.applicationContext == null) {
      SpringContextUtils.applicationContext = applicationContext;
    }
  }

  /**
   * 获取 applicationContext
   * @return ApplicationContext
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * 通过name获取 Bean.
   * @param name Bean 的名称
   * @return Bean 实例
   */
  public static Object getBean(String name) {
    return getApplicationContext().getBean(name);
  }

  /**
   * 通过class获取Bean.
   * @param clazz class
   * @param <T> 类名
   * @return Bean 实例
   */
  public static <T> T getBean(Class<T> clazz) {
    return getApplicationContext().getBean(clazz);
  }

  /**
   * 通过name,以及Clazz返回指定的Bean
   * @param name Bean 的名称
   * @param clazz class
   * @param <T> 类名
   * @return Bean 实例
   */
  public static <T> T getBean(String name, Class<T> clazz) {
    return getApplicationContext().getBean(name, clazz);
  }

  /**
   * 判断 Bean 是否存在
   * @param name Bean 的名称
   * @return true：存在， false：不存在
   */
  public static boolean containsBean(String name) {
    return applicationContext.containsBean(name);
  }

  /**
   * 判断 Bean 是否唯一
   * @param name Bean 的名称
   * @return true：唯一， false：不唯一
   */
  public static boolean isSingleton(String name) {
    return applicationContext.isSingleton(name);
  }

  /**
   * 获取 Bean 的类型
   * @param name Bean 的名称
   * @return Bean 的 Class 类型
   */
  public static Class<? extends Object> getType(String name) {
    return applicationContext.getType(name);
  }

}
