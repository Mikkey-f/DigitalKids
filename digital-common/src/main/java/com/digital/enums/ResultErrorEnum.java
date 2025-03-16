package com.digital.enums;

import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  09:05
 */
@Getter
public enum ResultErrorEnum {
    PARAM_IS_ERROR(100, "参数错误"),
    PARAM_IS_NULL(101, "参数不能为空"),
    ACCOUNT_IS_REPEAT(102, "账户重复"),
    PHONE_NUMBER_IS_REPEAT(103, "电话号码已经注册"),
    NEED_RIGHT_AUTH(104, "权限不足"),
    NO_AUTH_ERROR(105, "认证失败"),
    TOKEN_EXPIRED(106, "token过期"),
    TOKEN_ERROR(107, "会话错误"),
    THIS_USER_NOT_REGISTER(108, "没有注册"),
    PASSWORD_ERROR(109, "密码错误"),
    PARAM_INPUT_TOO_SHORT(110, "参数长度太短"),
    NOT_LOGIN_USER(111, "无登录用户"),
    OPERATION_ERROR(112, "操作错误"),

    W_PARAM_IS_NULL(201, "儿童不能为空"),

    SUCCESS(500, "成功!");
    Integer code;
    String message;

    ResultErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
