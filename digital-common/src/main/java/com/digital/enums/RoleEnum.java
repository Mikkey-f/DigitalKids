package com.digital.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-13  18:35
 */
@Getter
public enum RoleEnum {
    ADMIN(001, "admin"),
    USER(002, "user"),
    GUEST(003, "guest");

    private int code;
    private String value;
    RoleEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }
}
