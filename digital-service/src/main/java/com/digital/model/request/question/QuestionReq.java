package com.digital.model.request.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/23 14:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionReq implements Serializable {

    private static final long serialVersionUID = 1L;
    private String question;
}
