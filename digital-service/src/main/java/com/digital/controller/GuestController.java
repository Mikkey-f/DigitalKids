package com.digital.controller;

import com.digital.config.JwtConfig;
import com.digital.model.vo.LoginGuestVo;
import com.digital.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-13  17:37
 */
@RestController
@Slf4j
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    JwtConfig jwtConfig;

    @PostMapping("/login")
    public Result<LoginGuestVo> guestLogin() {
        // guestName guestName_bd7130c67a674c6bb9decb847cd21bf1
        String guestName = "guestName_" + UUID.randomUUID().toString().replace("-", "");
        String token = jwtConfig.createToken(guestName);
        LoginGuestVo loginGuestVo = new LoginGuestVo(guestName, token);

        return Result.success(loginGuestVo);
    }
}
