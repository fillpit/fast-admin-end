package com.admin.core.config.http;

import com.admin.core.exception.AppException;

/**
 * httpClient 请求响应错误处理
 * @author fei
 * @date 2018/6/24
 */
public class HttpResponseException extends AppException {
  public HttpResponseException(String errorMessage) {
    super(errorMessage);
  }

  public HttpResponseException(int errorCode, String errorMessage) {
    super(errorCode, errorMessage);
  }
}
