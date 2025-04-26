package com.digital.model.vo.tongue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/25 13:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TongueResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private String coated_result;
    private String color_result;
    private String crack_result;
    private String indent_result;
}
