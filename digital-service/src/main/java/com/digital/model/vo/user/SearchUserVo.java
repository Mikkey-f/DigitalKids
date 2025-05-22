package com.digital.model.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-21  16:17
 */
@Data
public class SearchUserVo implements Serializable {

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 用户头像
     */
    private String avatar;


    /**
     * 用户角色：user/admin/ban
     */
    private String role;

    private Object gender;

    private static final long serialVersionUID = 1L;
}
