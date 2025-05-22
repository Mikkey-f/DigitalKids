package com.digital.enums;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/26 21:24
 */
@Getter
public enum ParentingEncyStageEnum {
    BEI_YUN(0, "备孕期"),
    YUN_CHAN(1, "孕产期管理"),
    CHAN_RU(2, "产褥期"),
    CHAN_HOU(3, "产后"),
    ZERO_TO_ONE(4, "0到1岁"),
    ONE_TO_TWO(5, "1到2岁"),
    TWO_TO_THREE(6, "2到3岁"),
    THREE_TO_FIVE(7, "3到5岁"),
    TEN_TO_FIFTEEN(8, "10到15岁");

    private final int stage;
    private final String name;
    ParentingEncyStageEnum(int stage, String name) {
        this.stage = stage;
        this.name = name;
    }
}
