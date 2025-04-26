package com.digital.enums;

import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/20 12:06
 */
@Getter
public enum ArmValueEnum {
    YOUNG(25.0, 45.0, "幼儿时期"),
    CHILD(45.0, 75.0, "儿童时期"),
    TEENAGER(75.0, 95.0,  "少年时期");

    private final Double fromArmLength;
    private final Double toArmLength;
    private final String value;

    ArmValueEnum(Double fromArmLength, Double toArmLength, String value) {
        this.fromArmLength = fromArmLength;
        this.toArmLength = toArmLength;
        this.value = value;
    }
}
