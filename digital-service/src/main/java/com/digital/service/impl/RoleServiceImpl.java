package com.digital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.model.entity.Role;
import com.digital.service.RoleService;
import com.digital.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 普罗米修斯
* @description 针对表【role】的数据库操作Service实现
* @createDate 2025-03-12 20:45:49
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




