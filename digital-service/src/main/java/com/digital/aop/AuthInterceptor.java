package com.digital.aop;

import com.digital.annotation.AuthCheck;
import com.digital.enums.ResultErrorEnum;
import com.digital.enums.RoleEnum;
import com.digital.model.entity.User;
import com.digital.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-15  22:03
 */
@Aspect
@Component
public class AuthInterceptor {

    @Autowired
    private UserService userService;

    /**
     * 执行拦截
     * @param joinPoint
     * @param authCheck
     * @return
     * @throws Throwable
     */
    @Around("@annotation(authCheck)")
    public Object doIntercept(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new Exception(ResultErrorEnum.NO_AUTH_ERROR.getMessage());
        }
        // 鉴定权限
        if (StringUtils.isNotBlank(mustRole)) {
            // 要求身份，通过注解得到
            RoleEnum mustUserRoleEnum = RoleEnum.getEnumByValue(mustRole);
            if (mustUserRoleEnum == null) {
                throw new Exception(ResultErrorEnum.NO_AUTH_ERROR.getMessage());
            }
            String userRole = loginUser.getRole();
            // 如果被封号
            if (RoleEnum.BAN.getValue().equals(userRole)) {
                throw new Exception(ResultErrorEnum.NO_AUTH_ERROR.getMessage());
            }
            // 排除游客访问
            if (RoleEnum.USER.equals(mustUserRoleEnum)) {
                if (!userRole.equals(RoleEnum.ADMIN.getValue()) && !userRole.equals(RoleEnum.USER.getValue())) {
                    throw new Exception(ResultErrorEnum.NO_AUTH_ERROR.getMessage());
                }
            }

            if (RoleEnum.ADMIN.equals(mustUserRoleEnum)) {
                if (!userRole.equals(mustRole)) {
                    throw new Exception(ResultErrorEnum.NO_AUTH_ERROR.getMessage());
                }
            }
        }
        return joinPoint.proceed();
    }
}
