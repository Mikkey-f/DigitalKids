package com.digital.enums;

import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/19 22:30
 */
@Getter
public enum KidOldEnum {
    YOUNG(0, 3, "幼儿时期"),
    CHILD(4, 12, "儿童时期"),
    TEENAGER(13, 18, "少年时期");

    private int from;
    private int end;
    private String value;

    KidOldEnum(int from, int end, String value) {
        this.from = from;
        this.end = end;
        this.value = value;
    }
}
