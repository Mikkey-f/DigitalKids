package com.digital.enums;

import lombok.Data;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-13  18:35
 */
@Getter
public enum RoleEnum {
    ADMIN(001, "admin"),
    USER(002, "user"),
    GUEST(003, "guest"),
    BAN(004, "ban");

    private int code;
    private String value;
    RoleEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static RoleEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (RoleEnum anEnum : RoleEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
