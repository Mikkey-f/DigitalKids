package com.digital.model.request.threejs;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 21:00
 */
@Data
public class EndocrineReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long kidId;
    private String thyroidFunction;
    private String growthHormone;
    private String insulin;
    private String metabolicRate;
}
