package com.digital.model.request.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-16  15:21
 */
@Data
public class UserUpdateMyReq implements Serializable {

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 用户头像
     */
    private String avatar;

    private Object gender;

    private String password;

    private String phone;

    private String location;

    private static final long serialVersionUID = 1L;
}
