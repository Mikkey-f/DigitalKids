package com.digital.model.vo.threejs;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 20:43
 */
@Data
public class ThreeJsVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String description;
    private String icon;
    private Map<String, String> map;
    private String recommendation;
}
