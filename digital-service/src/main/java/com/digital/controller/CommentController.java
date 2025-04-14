package com.digital.controller;

import com.digital.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/14 19:11
 */
@RestController
@Slf4j
@RequestMapping("/comment")
public class CommentController {

    /**
     * 加
     * @return
     */
    @PostMapping(path = "/add}")
    public Result addComment() {

    }
}
