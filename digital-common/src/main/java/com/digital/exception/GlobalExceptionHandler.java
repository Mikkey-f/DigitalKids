package com.digital.exception;

import com.digital.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;


/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-16  14:12
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<?> businessExceptionHandler(BusinessException e, HttpServletResponse response) {
        log.error("BusinessException", e);

        // 检查响应是否已提交
        if (!response.isCommitted()) {
            return Result.error(e.getMessage());
        } else {
            log.warn("Response already committed, skipping error handling for BusinessException");
            return null;  // 可以选择返回 null 或做其他处理
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<?> runtimeExceptionHandler(RuntimeException e, HttpServletResponse response) {
        log.error("RuntimeException", e);

        // 检查响应是否已提交
        if (!response.isCommitted()) {
            return Result.error("系统错误");
        } else {
            log.warn("Response already committed, skipping error handling for RuntimeException");
            return null;  // 可以选择返回 null 或做其他处理
        }
    }
}
