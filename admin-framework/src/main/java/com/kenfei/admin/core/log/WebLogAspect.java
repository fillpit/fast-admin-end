package com.kenfei.admin.core.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kenfei.admin.core.utils.json.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 日志切面
 *
 * @author fei
 * @since 2019-05-20 14:18
 */
@Aspect
@Profile({"dev", "test"})
public class WebLogAspect {
  private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
  /** 换行符 */
  private static final String LINE_SEPARATOR = System.lineSeparator();

  /** 以自定义 @WebLog 注解为切点 */
  @Pointcut("@annotation(com.kenfei.admin.core.log.WebLog)")
  public void webLog() {}

  /**
   * 在切点之前织入
   *
   * @param joinPoint 切点
   * @throws ClassNotFoundException 类不存在异常
   */
  @Before("webLog()")
  public void doBefore(JoinPoint joinPoint) throws ClassNotFoundException, JsonProcessingException {
    // 获取 @WebLog 注解的描述信息
    String methodDescription = getAspectLogDescription(joinPoint);

    // 开始打印请求日志
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    // 打印请求相关参数
    logger.info(
        "========================================== Start ==========================================");
    if (null != attributes) {
      HttpServletRequest request = attributes.getRequest();
      // 打印请求 url
      logger.info("URL            : {}", request.getRequestURL().toString());
      // 打印 Http method
      logger.info("HTTP Method    : {}", request.getMethod());
      // 打印请求的 IP
      logger.info("IP             : {}", request.getRemoteAddr());
    }
    // 打印描述信息
    logger.info("Description    : {}", methodDescription);
    // 打印调用 controller 的全路径以及执行方法
    logger.info(
        "Class Method   : {}.{}",
        joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName());
    // 打印请求入参
    logger.info("Request Args   : {}", JSON.toString(joinPoint.getArgs()));

  }

  /**
   * 在切点之后织入
   */
  @After("webLog()")
  public void doAfter(){
    // 接口结束后换行，方便分割查看
    logger.info(
        "=========================================== End ==========================================="
            + LINE_SEPARATOR);
  }

  /**
   * 环绕
   *
   * @param proceedingJoinPoint 切面
   * @return 切面方法的返回值
   * @throws Throwable 方法执行异常
   */
  @Around("webLog()")
  public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();
    Object result = proceedingJoinPoint.proceed();
    // 打印出参
    logger.info("Response Args  : {}", JSON.toString(result));
    // 执行耗时
    logger.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
    return result;
  }

  /**
   * 获取切面注解的描述
   *
   * @param joinPoint 切点
   * @return 描述信息
   * @throws ClassNotFoundException 类不存在异常
   */
  private String getAspectLogDescription(JoinPoint joinPoint) throws ClassNotFoundException {
    String targetName = joinPoint.getTarget().getClass().getName();
    String methodName = joinPoint.getSignature().getName();
    Object[] arguments = joinPoint.getArgs();
    Class targetClass = Class.forName(targetName);
    Method[] methods = targetClass.getMethods();
    StringBuilder description = new StringBuilder();
    for (Method method : methods) {
      if (method.getName().equals(methodName)) {
        Class[] clazzs = method.getParameterTypes();
        if (clazzs.length == arguments.length) {
          description.append(method.getAnnotation(WebLog.class).description());
          break;
        }
      }
    }
    return description.toString();
  }
}
