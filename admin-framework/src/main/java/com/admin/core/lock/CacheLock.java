package com.admin.core.lock;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author fei
 * @since 2019-03-23 14:46
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheLock {

  /**
   * redis 锁key的前缀.
   *
   * @return redis 锁key的前缀
   */
  String value() default "";

  /**
   * 过期时间,默认为200毫秒.
   *
   * @return 轮询锁的时间
   */
  int expire() default 200;

  /**
   * 超时时间单位.
   *
   * @return 毫秒
   */
  TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

  /**
   * <p>Key的分隔符（默认 :）.</p>
   * <p>生成的Key：N:SO1008:200</p>
   *
   * @return String
   */
  String delimiter() default ":";
}
