package com.kenfei.admin.modules.quartz.utils;

import com.kenfei.admin.core.common.exception.AppException;

/**
 * @author fei
 * @since 2020/10/30 10:59 上午
 */
public class TaskException extends AppException {

  public TaskException(String errorMessage) {
    super(errorMessage);
  }

  public TaskException(String errorMessage, Throwable throwable) {
    super(errorMessage, throwable);
  }

  public TaskException(int errorCode, String errorMessage) {
    super(errorCode, errorMessage);
  }
}
