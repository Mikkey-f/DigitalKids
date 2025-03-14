package com.digital.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  20:14
 */
@Data
public class LoginUserVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户 id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 性别
     */
    private Object gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 地址
     */
    private String location;

    private String token;
<<<<<<< HEAD
=======

    private String role;
>>>>>>> 3e1045ede3b7b3d2e0b41ee8184db19d5df6faad
}
