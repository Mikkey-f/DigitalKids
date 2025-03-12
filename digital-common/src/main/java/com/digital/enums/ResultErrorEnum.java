package com.digital.enums;

import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  09:05
 */
@Getter
public enum ResultErrorEnum {
    PARAM_IS_NULL(1, "参数不能为空"),
    ACCOUNT_IS_REPEAT(2, "账户重复"),
    PHONE_NUMBER_IS_REPEAT(3, "电话号码已经注册");
    Integer code;
    String message;

    ResultErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
