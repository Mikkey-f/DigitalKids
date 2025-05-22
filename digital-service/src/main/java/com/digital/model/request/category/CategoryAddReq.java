package com.digital.model.request.category;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 15:23
 */
@Data
public class CategoryAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer parentId;

    private String name;

    private Integer status;
}
