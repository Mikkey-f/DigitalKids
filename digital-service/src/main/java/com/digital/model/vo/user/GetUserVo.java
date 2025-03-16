package com.digital.model.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-16  15:25
 */
@Data
public class GetUserVo implements Serializable {

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


    /**
     * 用户角色：user/admin/ban
     */
    private String role;

    /**
     * 创建时间
     */
    private Date createTime;

    private Object gender;

    private static final long serialVersionUID = 1L;
}
