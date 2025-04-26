package com.digital.model.request.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-16  15:35
 */
@Data
public class UserUpdateReq implements Serializable {
    /**
     * id
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


    private String location;

    private String password;

    private Object gender;

    private static final long serialVersionUID = 1L;
}
