package com.admin.core.resolvers;

import com.admin.core.annotations.JsonParam;
import com.admin.core.exception.AppException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 自定义参数装填.
 *
 * @author fei
 * @date 2018/03/19
 */
@Slf4j
public class CustomerArgumentResolver implements HandlerMethodArgumentResolver {

  private static final String CONTENT_TYPE = "application/json";
  private static final String JSON_BODY_ATTRIBUTE = "JSON_REQUEST_BODY";
  private ObjectMapper om = new ObjectMapper();

  /**
   * 处理过滤规则.
   *
   * @param methodParameter 方法参数
   * @return true: 拦截， false: 放行
   */
  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    return methodParameter.hasParameterAnnotation(JsonParam.class);
  }

  /**
   * 装载参数.
   *
   * @param methodParameter 方法参数
   * @param modelAndViewContainer 返回视图容器
   * @param nativeWebRequest 本次请求对象
   * @param webDataBinderFactory 数据绑定工厂
   * @return 装填后的参数
   */
  @Override
  public Object resolveArgument(
      MethodParameter methodParameter,
      ModelAndViewContainer modelAndViewContainer,
      NativeWebRequest nativeWebRequest,
      WebDataBinderFactory webDataBinderFactory)
      throws Exception {

    // content-type 不是json的不处理
    HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
    if (!request.getContentType().contains(CONTENT_TYPE)) {
      throw new AppException(
          "Request content types '" + request.getContentType() + "' not supported");
    }

    // 从 body 中获取参数
    String parameterName = methodParameter.getParameterName();
    String body = getRequestBody(nativeWebRequest);
    JsonNode rootNode = om.readTree(body);
    JsonNode node = rootNode.path(parameterName);

    return om.readValue(node.toString(), methodParameter.getParameterType());
  }

  private String getRequestBody(NativeWebRequest webRequest) {
    HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

    String jsonBody =
        (String) webRequest.getAttribute(JSON_BODY_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);
    if (jsonBody == null) {
      try {
        jsonBody = StreamUtils.copyToString(servletRequest.getInputStream(), UTF_8);
        webRequest.setAttribute(JSON_BODY_ATTRIBUTE, jsonBody, NativeWebRequest.SCOPE_REQUEST);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return jsonBody;
  }
}
