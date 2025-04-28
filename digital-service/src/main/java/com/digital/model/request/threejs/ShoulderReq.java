package com.digital.model.request.threejs;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 20:56
 */
@Data
public class ShoulderReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private String rangeOfMotion;
    private String painIndex;
    private String stability;
    private String muscleStrength;
    private Long kidId;
}
