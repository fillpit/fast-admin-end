package com.admin.core.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 方法锁.
 *
 * @author fei
 * @since 2019-03-23 16:50
 */
@Aspect
@Component
public class LockMethodAspect {
  private final StringRedisTemplate stringRedisTemplate;

  @Autowired
  public LockMethodAspect(StringRedisTemplate stringRedisTemplate) {
    this.stringRedisTemplate = stringRedisTemplate;
  }

  /**
   * 切入点.
   * @param pjp 切面对象
   * @return obj
   */
  @Around(value = "execution(public * *(..)) && @annotation(com.admin.core.lock.CacheLock)")
  public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
    MethodSignature signature = (MethodSignature) pjp.getSignature();
    Method method = signature.getMethod();
    CacheLock lock = method.getAnnotation(CacheLock.class);
    if (StringUtils.isEmpty(lock.value())) {
      throw new RuntimeException("lock key don't null...");
    }
    final String lockKey = this.getLockKey(pjp);
    String value = UUID.randomUUID().toString();
    // 假设上锁成功，但是设置过期时间失效，以后拿到的都是 false
    final boolean success = this.lock(lockKey, value, lock.expire(), lock.timeUnit());
    if (!success) {
      throw new RuntimeException(
          "请求速度超过该方法的限制(超时: " + lock.expire() + ", 单位: " + lock.timeUnit().name() + ")");
    }
    return pjp.proceed();
  }

  /**
   * 获取锁的 key.
   *
   * @param pjp 切面对象
   * @return key
   */
  private String getLockKey(ProceedingJoinPoint pjp) {
    MethodSignature signature = (MethodSignature) pjp.getSignature();
    Method method = signature.getMethod();
    CacheLock lockAnnotation = method.getAnnotation(CacheLock.class);
    final Object[] args = pjp.getArgs();
    final Parameter[] parameters = method.getParameters();
    StringBuilder builder = new StringBuilder();
    // 默认解析方法里面带 CacheParam 注解的属性
    for (int i = 0; i < parameters.length; i++) {
      final CacheParam annotation = parameters[i].getAnnotation(CacheParam.class);
      if (annotation == null) {
        continue;
      }
      builder.append(lockAnnotation.delimiter()).append(args[i]);
    }
    // 如果没有就是用该方法所有的参数当作key
    if (StringUtils.isEmpty(builder.toString())) {
      for (Object arg : args) {
        builder.append(lockAnnotation.delimiter()).append(arg);
      }
    }
    return lockAnnotation.value() + builder.toString();
  }

  /**
   * 获取锁.
   *
   * @param lockKey lockKey
   * @param uuid UUID
   * @param timeout 超时时间
   * @param unit 过期单位
   * @return true or false
   */
  private boolean lock(String lockKey, final String uuid, long timeout, final TimeUnit unit) {
    String oldVal = stringRedisTemplate.boundValueOps(lockKey).get();
    if (StringUtils.isEmpty(oldVal)) {
      stringRedisTemplate.boundValueOps(lockKey).set(uuid, timeout, unit);
    }
    return StringUtils.isEmpty(oldVal);
  }
}
