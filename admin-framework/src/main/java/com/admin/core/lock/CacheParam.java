package com.admin.core.lock;

import java.lang.annotation.*;

/**
 * 锁的参数.
 * @author fei
 * @since 2019-03-23 14:52
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheParam {
  /**
   * 字段名称.
   *
   * @return String
   */
  String name() default "";
}
