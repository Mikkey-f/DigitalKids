package com.digital.enums;

import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/19 22:49
 */
@Getter
public enum KidBodyValueEnum {

    YOUNG(15.5, 19.0, 100, 140, "幼儿时期"),
    CHILD(14.5, 21.4, 80, 100, "儿童时期"),
    TEENAGER(17.5, 23.5, 60, 90,  "少年时期");

    private final Double fromBmi;
    private final Double toBmi;
    private final Integer fromHeartBeat;
    private final Integer toHeartBeat;
    private final String value;

    KidBodyValueEnum (Double fromBmi, Double toBmi, Integer fromHeartBeat, Integer toHeartBeat, String value) {
        this.fromBmi = fromBmi;
        this.toBmi = toBmi;
        this.fromHeartBeat = fromHeartBeat;
        this.toHeartBeat = toHeartBeat;
        this.value = value;
    }
}
