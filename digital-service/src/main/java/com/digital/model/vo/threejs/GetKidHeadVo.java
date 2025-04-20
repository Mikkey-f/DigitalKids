package com.digital.model.vo.threejs;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/19 21:33
 */
@Data
@Builder
public class GetKidHeadVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Map<String, String> status;

    private String hairColor;

    private String eyeColor;

    private BigDecimal leftEyeDegree;

    private BigDecimal rightEyeDegree;

}
