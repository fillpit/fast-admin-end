package com.admin.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 参数检查异常
 *
 * @author fei
 * @date 2017/8/7
 */
@Slf4j
public class ParamNotValidException extends AppException {
  private static final long serialVersionUID = -716441870779206738L;
  private List<FieldError> fieldErrors;

  public ParamNotValidException(MethodArgumentNotValidException ex) {
    super("", ex);
    this.fieldErrors = bindExceptionToFieldError(ex.getBindingResult());
    this.setErrorMessage(this.fieldErrors.toString());
  }

  public ParamNotValidException(BindException ex) {
    super("", ex);
    this.fieldErrors = bindExceptionToFieldError(ex.getBindingResult());
    this.setErrorMessage(this.fieldErrors.toString());
  }

  public ParamNotValidException(ConstraintViolationException violationException) {
    super("", violationException);
    this.fieldErrors =
        violationException
            .getConstraintViolations()
            .stream()
            .map(
                cv -> {
                  String paramName = cv.getPropertyPath().toString();
                  FieldError error = new FieldError();
                  error.setName(paramName);
                  error.setMessage(cv.getMessage());
                  return error;
                })
            .collect(Collectors.toList());
    this.setErrorMessage(this.fieldErrors.toString());
  }

  @Override
  public String getMessage() {
    return fieldErrors.toString();
  }

  private List<FieldError> bindExceptionToFieldError(BindingResult bindingResult) {
    return bindingResult
        .getFieldErrors()
        .stream()
        .map(
            f -> {
              FieldError error = new FieldError();
              error.setName(f.getObjectName().concat(".").concat(f.getField()));
              error.setMessage(f.getDefaultMessage());
              return error;
            })
        .collect(Collectors.toList());
  }

  /** 自定义提示内容 */
  class FieldError {
    /** 字段名 */
    private String name;
    /** 错误信息 */
    private String message;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    @Override
    public String toString() {
      return String.format("`%s`: %s", name, message);
    }
  }
}
