package com.digital.model.vo.threejs;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/19 21:34
 */
@Data
@Builder
public class GetRightLegVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, String> status = new ConcurrentHashMap<String, String>();

    private BigDecimal legLength;
}
