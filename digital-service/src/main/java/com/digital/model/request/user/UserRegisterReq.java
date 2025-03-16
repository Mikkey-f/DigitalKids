package com.digital.model.request.user;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  10:07
 */

import lombok.Data;


@Data
public class UserRegisterReq {

    private String username;

    private String password;

    private String avatar;

    private String phone;

    private String role;

    private Object gender;
}
