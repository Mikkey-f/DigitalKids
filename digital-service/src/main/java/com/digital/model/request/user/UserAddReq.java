package com.digital.model.request.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-16  15:15
 */
@Data
public class UserAddReq implements Serializable {
    private String name;
    private String phone;
    private String avatar;
    private String role;
    private String location;
    private Object gender;
    private String password = "123456";
    private static final long serialVersionUID = 1L;
}
