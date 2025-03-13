package com.digital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.model.entity.Permission;
import com.digital.service.PermissionService;
import com.digital.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

/**
* @author 普罗米修斯
* @description 针对表【permission】的数据库操作Service实现
* @createDate 2025-03-12 20:45:23
*/
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
    implements PermissionService{

}




