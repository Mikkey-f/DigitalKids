package com.digital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digital.enums.ResultErrorEnum;
import com.digital.exception.BusinessException;
import com.digital.mapper.*;
import com.digital.model.entity.*;
import com.digital.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  20:24
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userRepository;
    @Autowired
    private UserRoleMapper userRoleRepository;
    @Autowired
    private RoleMapper roleRepository;
    @Autowired
    private PermissionMapper permissionRepository;
    @Autowired
    private RolePermissionMapper rolePermissionRepository;


    @Override
    public UserDetails loadUserByUsername(String phoneNum){
        // 查询用户信息
        User user = userRepository.selectOne(new QueryWrapper<User>().eq("phone",phoneNum));
        if (user == null) {
            throw new BusinessException(ResultErrorEnum.NOT_LOGIN_USER);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();

        List<UserRole> userRoles = userRoleRepository.selectList(new QueryWrapper<UserRole>().eq("user_id",user.getId()));
        //用户信息查询完毕后,根据关联表查询用户的多个角色
        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        List<Role> roles = roleRepository.selectBatchIds(roleIds);

        // 1. 查询角色权限关联关系
        List<RolePermission> rolePermissions = rolePermissionRepository.selectList(
                new LambdaQueryWrapper<RolePermission>()
                        .in(RolePermission::getRoleId, roleIds)
        );

        // 2. 提取权限ID集合
        List<Long> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());

        // 3. 批量查询权限信息
        List<Permission> permissions = permissionRepository.selectBatchIds(permissionIds);

        // 添加角色（ROLE_前缀）
        roles.forEach(role ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()))
        );

        // 添加权限（直接使用权限名称）
        permissions.forEach(permission ->
                authorities.add(new SimpleGrantedAuthority(permission.getName()))
        );

        return new CustomUserDetails(user, authorities);
    }
}
