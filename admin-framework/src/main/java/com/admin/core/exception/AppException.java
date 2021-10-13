package com.admin.core.exception;

/**
 * 自定义异常基类
 *
 * @author fei
 * @date 2017/10/2
 */
public class AppException extends RuntimeException {
  private static final long serialVersionUID = 2404372373182554123L;

  /** 错误码 */
  private int errorCode;
  /** 异常信息 */
  private String errorMessage;
  /** 异常栈 */
  private Throwable throwable;

  /**
   * 设置错误信息
   *
   * @param errorMessage 错误信息
   */
  public AppException(String errorMessage) {
    super(errorMessage);
    this.throwable = this;
    this.errorMessage = errorMessage;
  }

  /**
   * 设置错误信息
   *
   * @param errorMessage 错误信息
   * @param throwable 异常对象
   */
  public AppException(String errorMessage, Throwable throwable) {
    super(errorMessage);

    this.errorMessage = errorMessage;
    this.throwable = throwable;
  }

  public AppException(int errorCode, String errorMessage) {
    super(errorMessage);

    this.throwable = this;
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public Throwable getThrowable() {
    return throwable;
  }

  public void setThrowable(Throwable throwable) {
    this.throwable = throwable;
  }
}
