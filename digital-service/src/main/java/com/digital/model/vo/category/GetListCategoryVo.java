package com.digital.model.vo.category;

import lombok.Data;

import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 15:16
 */
@Data
public class GetListCategoryVo {

    private Integer id;

    private Integer parentId;

    private String name;

    private List<GetListCategoryVo> subCategories;
}
