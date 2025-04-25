package com.digital.enums;

import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/25 20:54
 */
@Getter
public enum PermissionEnum {

    READ_ONLY(1, "read_only"),
    WRITE_ONLY(2, "write_only"),
    READ_WRITE(3, "read_write");

    private Integer id;
    private String name;

    PermissionEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
