package com.kenfei.admin.core.common.exception;


import com.kenfei.admin.core.common.base.BaseEnum;

/**
 * 异常错误编码
 *
 * @author fei
 * @date 2017/8/7
 */
public enum ErrorCodeEnum implements BaseEnum<Integer> {
  /** 未知异常 */
  UNKNOWN_EXCEPTION(1000, "未知异常"),
  UN_AUTHORIZATION(1001, "无访问权限"),
  USER_ACCESS_NOT_INIT(1002, "用户没有初始化"),
  INVALID_PARAMS(1003, "无效参数: %s"),
  INVALID_CLASS_COPY(1004, "类复制失败"),
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
