package com.kenfei.admin.core.config.resolvers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.kenfei.admin.core.common.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

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
  private final ObjectMapper mapper = new ObjectMapper();

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
      throws JsonProcessingException, ClassNotFoundException {

    // content-type 不是json的不处理
    HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
    if (!request.getContentType().contains(CONTENT_TYPE)) {
      throw new AppException(
          "Request content types '" + request.getContentType() + "' not supported");
    }

    // 从 body 中获取参数
    String parameterName = methodParameter.getParameterName();
    String body = getRequestBody(nativeWebRequest);
    JsonNode rootNode = mapper.readTree(body);
    JsonNode node = rootNode.path(parameterName);

    Type parameterType = methodParameter.getGenericParameterType();
    if (parameterType instanceof ParameterizedType) {
      // 接受复杂的参数类型(比如 List<?>)
      return complexType(node.toString(), methodParameter);
    }
    // 简单参数
    return basicType(node.toString(), methodParameter);
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

  /**
   * 基本类型参数（比如：String，Integer）
   * @param node 节点数据
   * @param methodParameter 方法参数
   * @return /
   */
  private Object basicType(String node, MethodParameter methodParameter) throws JsonProcessingException {

    // 如果没传指定的参数就返回 null 而不是抛出异常
    try {
      return mapper.readValue(node, methodParameter.getParameterType());
    } catch (MismatchedInputException ex) {
      return null;
    }
  }

  /**
   * 复杂的参数类型
   * @param node 节点数据
   * @param methodParameter 方法参数
   * @return /
   * @throws JsonProcessingException /
   */
  private Object complexType(String node, MethodParameter methodParameter) throws JsonProcessingException, ClassNotFoundException {

    ParameterizedType parameterType = (ParameterizedType) methodParameter.getGenericParameterType();
    Type parametrized = parameterType.getRawType();
    Type[] parameterClasses = parameterType.getActualTypeArguments();

    Class<?> a = Class.forName(parametrized.getTypeName());
    Class<?>[] b = getParameterTypeClass(parameterClasses);

    JavaType javaType = getCollectionType(a, b);

    // 如果没传指定的参数就返回 null 而不是抛出异常
    try {
      return mapper.readValue(node, javaType);
    } catch (MismatchedInputException ex) {
      return null;
    }
  }


  /**
   * element
   *
   * <p>获取泛型的Collection Type
   *
   * @param collectionClass 泛型的Collection
   * @param elementClasses 元素类
   * @return JavaType Java类型
   * @since 1.0
   */
  public JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
    return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
  }

  public Class<?>[] getParameterTypeClass(Type[] parameter) throws ClassNotFoundException {
    if (Objects.isNull(parameter) || parameter.length == 0) {
      return null;
    }

    Class<?>[] classes= new Class[parameter.length];
    for(int i = 0; i < parameter.length; ++i) {
      classes[i] = Class.forName(parameter[i].getTypeName());
    }

    return classes;
  }
}

