package com.admin.core.exception;

/**
 * 403 Unauthorized - [*]
 * 表示用户没有权限
 *
 * @author fei
 * @date 2017/10/2
 */
public class AuthorizationException extends AppException {
    private static final long serialVersionUID = 3546823786190714308L;

    public AuthorizationException() {
        super(ErrorCodeEnum.UN_AUTHORIZATION.value(), ErrorCodeEnum.UN_AUTHORIZATION.note());
    }

    public AuthorizationException(String errorMessage) {
        super(errorMessage);
    }

    public AuthorizationException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
