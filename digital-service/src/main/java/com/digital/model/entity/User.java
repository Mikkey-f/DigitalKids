package com.digital.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;

/**
 * @TableName users
 */
@Data
@TableName("user")
@Getter
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String password;

    private String avatar;

    private Object gender;

    private String phone;

    private String location;

    private String role;

    @TableField("is_delete")
    private String isDelete;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;


}