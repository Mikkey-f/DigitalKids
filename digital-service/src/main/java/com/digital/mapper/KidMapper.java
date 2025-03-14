package com.digital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digital.model.entity.Kid;

/**
 * @author Mr.wang
 * @description 针对表【Kid】的数据库操作Mapper
 * @createDate 2025-03-13 16:45:23
 * @Entity com.digital.Kid
 */
public interface KidMapper extends BaseMapper<Kid> {
    // 继承BaseMapper后，默认包含基础的CRUD方法
}
