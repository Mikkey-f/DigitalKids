package com.digital.enums;

import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 21:24
 */
@Getter
public enum ThreeJsEnum {
    HAND("手部精细功能", 1),
    FOOT("足部生物力学", 2),
    BODY("脊柱与核心肌群", 3),
    ARM("上肢功能状态",4),
    HEAD("神经系统和感官功能", 5),
    LEG("下肢肌力与关节功能", 6),
    SHOULDER("肩关节活动度与疼痛情况", 7),
    VISUAL("视力和散光情况", 8),
    ORAL("牙齿健康状况", 9),
    ENDOCRINE("激素与代谢", 10),
    ENT("听力与呼吸道", 11),
    RESPIRATORY("肺功能与呼吸", 12);

    private final String name;
    private final Integer code;

    ThreeJsEnum(String name, Integer code) {
        this.name = name;
        this.code = code;
    }
}
