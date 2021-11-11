package com.kenfei.admin.core.common.base;

import lombok.Builder;
import lombok.Data;

/**
 * 返回值格式
 *
 * @author fei
 * @date 2017/10/2
 */
@Data
@Builder
public class Response<T> {
  /** 请求结果. */
  private boolean success;
  /** 信息编码编码. */
  private String code;
  /** 返回信息. */
  private String message;
  /** 异常堆栈. */
  private String stack;
  /** 具体返回结果. */
  private T data;
}
