package com.digital.enums;

import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  10:10
 */
@Getter
public enum ResultSuccessEnum {
    REGISTER_SUCCESS(1, "注册成功");

    private int code;
    private String msg;
    ResultSuccessEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
