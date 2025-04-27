package com.digital.model.request.threejs;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/19 20:15
 */
@Data
@Slf4j
public class LeftArmUpdateReq {

    private Long kidId;

    private BigDecimal armLength;
}
