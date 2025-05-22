package com.digital.exception;

import com.digital.enums.ResultErrorEnum;
import com.digital.result.Result;
import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-16  14:12
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultErrorEnum errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ResultErrorEnum errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

}
