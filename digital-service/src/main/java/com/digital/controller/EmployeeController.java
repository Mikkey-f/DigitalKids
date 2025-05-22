package com.digital.controller;


import com.digital.annotation.AuthCheck;
import com.digital.result.Result;
import com.digital.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class EmployeeController {

    @Autowired
    UserService userService;

    @PostMapping("/hello")
    @AuthCheck(mustRole = "user")
    public Result hello(HttpServletRequest request) {

        log.info("{}", userService.getLoginUser(request));
        return Result.success("hello big shit");
    }
}
