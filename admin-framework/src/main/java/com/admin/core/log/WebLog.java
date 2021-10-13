package com.admin.core.log;

import java.lang.annotation.*;

/**
 * web 日志记录注解
 *
 * @author fei
 * @since 2019-05-20 14:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface WebLog {
  /** 描述. */
  String description() default "";
}
