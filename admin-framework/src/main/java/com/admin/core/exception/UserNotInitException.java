package com.admin.core.exception;

/**
 * 用户没有初始化 (401)
 *
 * @author fei
 * @date 2017/10/2
 */
public class UserNotInitException extends AppException {
  private static final long serialVersionUID = -5595096305303663654L;

  public UserNotInitException() {
    super("用户没有初始化");
  }

  public UserNotInitException(String message) {
    super(message);
  }

  public UserNotInitException(int errorCode, String errorMessage) {
    super(errorCode, errorMessage);
  }

}
