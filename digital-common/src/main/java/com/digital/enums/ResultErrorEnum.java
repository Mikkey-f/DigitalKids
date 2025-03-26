package com.digital.enums;

import lombok.Getter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  09:05
 */
@Getter
public enum ResultErrorEnum {
    PARAM_IS_ERROR(100, "参数错误"),
    PARAM_IS_NULL(101, "参数不能为空"),
    ACCOUNT_IS_REPEAT(102, "账户重复"),
    PHONE_NUMBER_IS_REPEAT(103, "电话号码已经注册"),
    NEED_RIGHT_AUTH(104, "权限不足"),
    NO_AUTH_ERROR(105, "认证失败"),
    TOKEN_EXPIRED(106, "token过期"),
    TOKEN_ERROR(107, "会话错误"),
    THIS_USER_NOT_REGISTER(108, "没有注册"),
    PASSWORD_ERROR(109, "密码错误"),
    PARAM_INPUT_TOO_SHORT(110, "参数长度太短"),
    NOT_LOGIN_USER(111, "无登录用户"),
    OPERATION_ERROR(112, "操作错误"),
    NOT_ALLOW_ADD_SAME_THING(113, "不能添加同一个物品"),
    MQ_ERROR(114, "消息队列错误"),
    MQ_GESHI_ERROR(115, "消息队列格式错误"),
    QUESTION_RESULT_NOT_FIND(116, "问题结果没有得到，请继续轮询"),
    SMART_AGENT_ERROR(117, "智能体错误"),
    CART_WITH_NO_PRODUCT(118, "购物车没有该商品"),


    W_PARAM_IS_NULL(201, "儿童不能为空"),
    W_FAIL_TO_SELECT(202,"查询育儿百科失败"),
    W_FAIL_TO_DELETE(203,"删除育儿百科失败"),
    W_FAIL_TO_DELETE_NOT_ID(204,"ID为NULL,删除育儿百科失败"),
    W_FAIL_TO_ADD(205,"添加育儿百科失败"),
    W_FAIL_TO_UPDATE(206,"修改育儿百科失败"),
    W_REQUEST_IS_NULL(207,"育儿百科请求为空"),
    W_ENCYCLOPEDIA_ID_IS_NULL(208,"育儿百科ID为空"),
    W_ENCYCLOPEDIA_IS_NULL(209,"育儿百科为空"),
    W_ENCYCLOPEDIA_FAIL_TO_UPDATE(210,"育儿百科更新失败"),
    W_ENCYCLOPEDIA_USER_ID_IS_NULL(211,"育儿百科ID为空"),
    W_ENCYCLOPEDIA_TITLE_IS_NULL(212,"育儿百科标题为空"),
    W_ENCYCLOPEDIA_CONTENT_IS_NULL(213,"育儿百科内容为空"),
    W_ENCYCLOPEDIA_STAGE_IS_NULL(214,"未选择育儿百科的对应阶段或者不合法"),
    W_ENCYCLOPEDIA_TITLE_IS_EXIST(215,"有相同的标题存在"),
    W_ENCYCLOPEDIA_ID_IS_EXIST(216,"有相同的ID存在"),

    SUCCESS(500, "成功!");
    Integer code;
    String message;

    ResultErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
