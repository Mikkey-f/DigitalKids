package com.digital.enums;

import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/20 12:06
 */
@Getter
public enum LegValueEnum {
    YOUNG(25.0, 45.0, "幼儿时期"),
    CHILD(45.0, 75.0, "儿童时期"),
    TEENAGER(75.0, 95.0,  "少年时期");

    private final Double fromLegLength;
    private final Double toLegLength;
    private final String value;

    LegValueEnum(Double fromLegLength, Double toLegLength, String value) {
        this.fromLegLength = fromLegLength;
        this.toLegLength = toLegLength;
        this.value = value;
    }
}
