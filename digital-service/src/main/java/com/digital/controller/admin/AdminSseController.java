package com.digital.controller.admin;

import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.result.Result;
import com.digital.utils.SseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/27 14:27
 */
@RestController
@Slf4j
@RequestMapping("/admin/sse")
public class AdminSseController {

    @Autowired
    private SseUtil sseUtil;

    /**
     * 管理员获得所有sse连接
     * @return 返回所有连接
     */
    @GetMapping("/listSseConnect")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Map<Long, SseEmitter>> listSseConnect() {
        Map<Long, SseEmitter> sseEmitterMap = sseUtil.listSseConnect();
        return Result.success(sseEmitterMap);
    }
}
