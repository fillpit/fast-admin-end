package com.kenfei.admin.core.common.base;


import org.springframework.beans.BeanUtils;

/**
 * dto 转换工具
 * @author fei
 * @date 2017/10/11
 */
public interface DtoConvert<T> {
  /* 分组校验 */
  @interface Sava {}

  /* 分组校验 */
  @interface Update {}

  /**
   * dto -> entity
   *
   * @param tClass entity class
   * @return entity
   */
  default T convert(Class<T> tClass) {
    T t = BeanUtils.instantiateClass(tClass);
    this.pathProperties(t);
    return t;
  }

  /**
   * dto -> entity
   *
   * @param tClass entity class
   * @return entity
   */
  default T convert(Class<T> tClass, String... ignoreProperties) {
    T t = BeanUtils.instantiateClass(tClass);
    this.pathProperties(t, ignoreProperties);

    return t;
  }

  /**
   * dto -> entity
   *
   * @param t entity
   */
  default T pathProperties(T t) {
    BeanUtils.copyProperties(this, t);

    return this.convertProperties(t);
  }

  /**
   * dto -> entity
   *
   * @param t entity
   * @param ignoreProperties 要忽略的属性
   * @return 源对象
   */
  default T pathProperties(T t, String... ignoreProperties) {
    BeanUtils.copyProperties(this, t, ignoreProperties);

    return this.convertProperties(t);
  }

  /**
   * 转换属性值
   * @param t entity 对象
   * @return entity 对象
   */
  default T convertProperties(T t) {
    return t;
  }
}
