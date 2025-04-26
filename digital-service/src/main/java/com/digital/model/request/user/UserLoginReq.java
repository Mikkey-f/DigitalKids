package com.digital.model.request.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  21:47
 */
@Data
public class UserLoginReq implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;

    private String phone;

    private String password;

    private String code;
}
