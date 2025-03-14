package com.digital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digital.model.entity.Kid;
import com.digital.result.Result;

import java.math.BigDecimal;


public interface KidService extends IService<Kid> {
    Result add(String avatar, String nickname, String birthdate, BigDecimal height, BigDecimal weight);
}

