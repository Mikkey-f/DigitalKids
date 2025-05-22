package com.digital.enums;

import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  10:10
 */
@Getter
public enum ResultSuccessEnum {
    REGISTER_SUCCESS(1, "注册成功"),
    ADD_SUCCESS(2, "添加成功"),
    W_ENCYCLOPEDIA_DELETE_SUCCESS(201,"育儿百科删除成功"),
    W_ENCYCLOPEDIA_UPDATE_SUCCESS(202,"育儿百科更新成功"),
    W_ENCYCLOPEDIA_ADD_SUCCESS(203,"育儿百科添加成功");

    private final int code;
    private final String msg;
    ResultSuccessEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
