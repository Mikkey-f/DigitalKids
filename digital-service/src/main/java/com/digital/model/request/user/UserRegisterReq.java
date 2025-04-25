package com.digital.model.request.user;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  10:07
 */

import lombok.Data;


@Data
public class UserRegisterReq {

    private String name;

    private String password;

    private String phone;

    private Object gender;

    private String code;
}
