package com.admin.core.advices;

import com.admin.core.exception.AppException;
import com.admin.core.exception.AuthorizationException;
import com.admin.core.exception.ErrorCodeEnum;
import com.admin.core.exception.ParamNotValidException;
import com.admin.core.exception.UserNotInitException;
import com.admin.core.repository.Response;
import com.admin.core.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * 统一异常处理
 *
 * @author fei
 * @since 2017/7/27
 */
@RestControllerAdvice
public class ControllerExceptionAdvice {
  private static final Logger log = LoggerFactory.getLogger(ControllerExceptionAdvice.class);
  private static final String DEFAULT_ERROR_MESSAGE = "系统遇到不可抗力奔溃了!!!";

  /**
   * 异常基类，所有自定义异常的父类.
   *
   * @param ex 异常对象
   * @return 400 状态码
   */
  @ExceptionHandler(value = AppException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public Response appException(AppException ex) {
    return appExceptionToResult(ex);
  }

  @ExceptionHandler(value = RuntimeException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public Response runtimeExceptionHandler(RuntimeException ex) {
    return appExceptionToResult(new AppException(ex.getMessage(), ex));
  }

  /**
   * 无权访问异常
   *
   * @return 403 状态码
   */
  @ExceptionHandler(AuthorizationException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public Response authException(AuthorizationException ex) {
    ex.setErrorCode(ErrorCodeEnum.UN_AUTHORIZATION.value());
    ex.setErrorMessage(ErrorCodeEnum.UN_AUTHORIZATION.note());
    return appExceptionToResult(ex);
  }

  /**
   * 木有登陆
   *
   * @return 401 异常状态码
   */
  @ExceptionHandler(UserNotInitException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public Response usernotinit(UserNotInitException ex) {
    ex.setErrorCode(ErrorCodeEnum.USER_ACCESS_NOT_INIT.value());
    ex.setErrorMessage(ErrorCodeEnum.USER_ACCESS_NOT_INIT.note());
    return appExceptionToResult(ex);
  }

  /**
   * 非法请求异常
   *
   * @param ex 异常对象
   * @return 400 状态码
   */
  @ExceptionHandler(ParamNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response paramNotValidExceptionHandler(ParamNotValidException ex) {
    ex.setErrorCode(ErrorCodeEnum.INVALID_PARAMS.value());
    return appException(ex);
  }

  /**
   * 请求参数不符合要求时抛出的异常(400 异常) 由 spring @Validated 验证所抛出的异常 验证范围，整个应用
   *
   * @param ex 异常对象
   * @return 错误提示
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public Response constraintViolationExceptionHandler(ConstraintViolationException ex) {
    return paramNotValidExceptionHandler(new ParamNotValidException(ex));
  }

  /**
   * 请求参数不符合要求时抛出的异常(400 异常) 由 spring @Validated @Valid 验证所抛出的异常 验证范围，controller 的对象类型数据
   * 好像更新版本后就由这个替换了 {@link BindException}
   *
   * @param ex 异常对象
   * @return 错误提示
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public Response bindExceptionHandler(MethodArgumentNotValidException ex) {
    return paramNotValidExceptionHandler(new ParamNotValidException(ex));
  }

  /**
   * 请求参数不符合要求时抛出的异常(400 异常) 由 spring @Validated @Valid 验证所抛出的异常 验证范围，controller 的对象类型数据
   *
   * @param ex 异常对象
   * @return 错误提示
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BindException.class)
  public Response bindException(BindException ex) {
    return paramNotValidExceptionHandler(new ParamNotValidException(ex));
  }

  /**
   * 内部服务器错误
   *
   * @param ex 异常对象
   * @return 500 状态码
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Response exception(Exception ex) {
    return appExceptionToResult(new AppException(DEFAULT_ERROR_MESSAGE, ex));
  }

  private Response appExceptionToResult(AppException ex) {
    log.error(ex.getThrowable().getMessage(), ex.getThrowable());
    return Response.builder()
        .success(false)
        .code(Objects.toString(ex.getErrorCode()))
        .message(ex.getErrorMessage())
        .stack(ExceptionUtil.getStackTrace(ex.getThrowable(), 20))
        .build();
  }
}
