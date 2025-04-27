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
    CART_IS_NULL(119, "购物车为空无法生成订单"),
    USER_ADDRESS_IS_NULL(120, "用户没有添加地址"),
    NOT_HAVE_THIS_ORDER(121, "数据库没有该订单"),
    THIS_ORDER_ALREADY_CANCELED(122, "该表单已经被取消"),
    ORDER_TYPE_ERROR(123,"订单类型错误，不能执行以下操作"),
    REDIS_TRANSACTION_ERROR(124, "redis事务运行失败"),
    ONLY_GENERATE_4OR6_CODE(125, "只能生成4或者6的验证码"),
    NOT_GET_CODE(126, "没有得到验证码"),
    LOGIN_IS_FAILURE(127, "登录失败"),
    REGISTER_IS_FAILURE(128, "注册失败"),
    KID_BODY_IS_LOADED(129, "该孩子身体信息已经载入"),
    KID_HEAD_IS_LOADED(130, "该孩子头部信息已经载入"),
    LEFT_ARM_IS_LOADED(131, "该孩子左臂信息已经载入"),
    RIGHT_ARM_IS_LOADED(132, "该孩子右臂信息已经载入"),
    LEFT_LEG_IS_LOADED(133, "该孩子左腿信息已经载入"),
    RIGHT_LEG_IS_LOADED(134, "该孩子右腿信息已经载入"),
    NOT_HAVE_THIS_KID(135, "没有这个孩子的信息"),
    KID_BODY_UNLOADED(136, "该孩子身体信息没有载入或不完整"),
    KID_HEAD_UNLOADED(137, "该孩子头部信息没有载入或不完整"),
    LEFT_ARM_UNLOADED(138, "该孩子左臂信息没有载入或不完整"),
    RIGHT_ARM_UNLOADED(139, "该孩子右臂信息没有载入或不完整"),
    LEFT_LEG_UNLOADED(140, "该孩子左腿信息没有载入或不完整"),
    RIGHT_LEG_UNLOADED(141, "该孩子右腿信息没有载入或不完整"),
    PLEASE_SELECT_IMAGE(142, "请选择想上传的图片"),
    FILE_UPLOAD_ERROR(143, "文件上传失败"),
    AUTH_IS_OVERTIME(144, "令牌已经过期"),
    FILE_OPERATION_ERROR(145, "文件处理错误"),
    AVATAR_UPLOAD_ERROR(146, "头像上传失败"),
    FILE_UPLOAD_IS_EMPTY(147, "头像为空，重新上传"),
    DB_DONT_HAVE_THIS_USER(148, "数据库没有这个用户"),
    FAVORITE_DELETE_IS_ERROR(149, "收藏删除错误"),
    NOT_SEARCH_CONTENT(150, "无搜索内容"),
    SEND_PRIVATE_MESSAGE_ERROR(151, "发送私信失败"),
    KID_NOT_LOADED(152, "孩子未被载入"),

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
    private final Integer code;
    private final String message;

    ResultErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
