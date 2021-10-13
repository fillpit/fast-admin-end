package com.admin.core.advices;

import com.admin.core.repository.Response;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Type;

/**
 * 返回值统一处理
 *
 * @author fei
 * @date 2017/10/2
 */
@RestControllerAdvice
public class CustomerResponseBodyAdvice implements ResponseBodyAdvice<Object> {

  /** 拦截逻辑 */
  @Override
  public boolean supports(
      MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    Type type = returnType.getGenericParameterType();
    // 如果返回值是 Result 类型就不用拦截了，我拦截就是要封装成 Result
    boolean noAware = Response.class.equals(type);
    return !noAware;
  }

  /**
   * 拦截实现
   *
   * @param body 返回内容
   * @param returnType 返回类型
   * @param request 请求对象
   * @param response 返回对象
   */
  @Override
  public Object beforeBodyWrite(
      Object body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {

    return Response.builder().success(true).code(HttpStatus.OK.toString()).data(body).build();
  }
}
