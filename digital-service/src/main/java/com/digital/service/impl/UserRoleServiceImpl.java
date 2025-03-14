package com.digital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.model.entity.UserRole;
import com.digital.service.UserRoleService;
import com.digital.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 普罗米修斯
* @description 针对表【user_role】的数据库操作Service实现
* @createDate 2025-03-12 20:46:20
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




