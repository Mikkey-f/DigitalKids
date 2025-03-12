package com.digital.enums;

import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  09:05
 */
@Getter
public enum ResultErrorEnum {
    PARAM_IS_NULL(101, "参数不能为空"),
    ACCOUNT_IS_REPEAT(102, "账户重复"),
    PHONE_NUMBER_IS_REPEAT(103, "电话号码已经注册"),
    NEED_RIGHT_AUTH(104, "权限不足"),
    NO_AUTH_ERROR(105, "认证失败"),
    TOKEN_EXPIRED(106, "token过期"),
    TOKEN_ERROR(107, "会话错误"),
    SUCCESS(500, "成功!");
    Integer code;
    String message;

    ResultErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
