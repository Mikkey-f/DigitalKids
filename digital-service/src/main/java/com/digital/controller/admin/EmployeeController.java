package com.digital.controller.admin;


import com.digital.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @PostMapping("/hello")
    public Result hello(){
        return Result.success("hello big shit");
    }
}
