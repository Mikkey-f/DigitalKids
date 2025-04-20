package com.digital.enums;

import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/19 22:36
 */
@Getter
public enum KidHeadValueEnum {
    YOUNG(200, 400, "幼儿时期"),
    CHILD(100, 200, "儿童时期"),
    TEENAGER(0, 100, "少年时期");

    private final Integer eyeFrom;
    private final Integer eyeTo;
    private final String value;

    KidHeadValueEnum(int eyeFrom, int eyeTo, String value) {
        this.eyeFrom = eyeFrom;
        this.eyeTo = eyeTo;
        this.value = value;
    }
}
