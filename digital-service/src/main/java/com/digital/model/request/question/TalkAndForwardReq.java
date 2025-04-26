package com.digital.model.request.question;

import com.digital.model.request.page.PageReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/22 17:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TalkAndForwardReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private String question;
    private PageReq pageReq;
}
