package com.digital.enums;

import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-16  17:20
 */
@Getter
public enum FavouriteTypeEnum {
    COMMENT(1, "评论"),
    VIDEO(2, "视频"),
    POST(3, "帖子");

    private final int code;
    private final String value;
    FavouriteTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

}
