package com.admin.core.exception;

import com.admin.core.enums.BaseEnum;

/**
 * 异常错误编码
 *
 * @author fei
 * @date 2017/8/7
 */
public enum ErrorCodeEnum implements BaseEnum<Integer> {
  /** 未知异常 */
  UNKNOW_EXCEPTION(0, "未知异常"),
  UN_AUTHORIZATION(10, "无访问权限"),
  USER_ACCESS_NOT_INIT(11, "用户没有初始化"),
  INVALID_PARAMS(12, "无效参数: %s"),
  INVALID_CLASS_COPY(13, "类复制失败"),
  ;

  private int code;
  private String msg;

  ErrorCodeEnum(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  @Override
  public String toString() {
    return Integer.toString(this.code);
  }

  @Override
  public Integer value() {
    return this.code;
  }

  public String note() {
    return this.msg;
  }
}
