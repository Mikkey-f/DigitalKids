package com.digital.controller;


import com.digital.annotation.AuthCheck;
import com.digital.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @PostMapping("/hello")
    @AuthCheck(mustRole = "admin")
    public Result hello(){
        return Result.success("hello big shit");
    }
}
