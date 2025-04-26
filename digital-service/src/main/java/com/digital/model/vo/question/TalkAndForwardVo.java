package com.digital.model.vo.question;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/22 17:12
 */
@Data
@NoArgsConstructor
public class TalkAndForwardVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer productId;
    private Map<String, String> message;
    private Integer parentingEncyclopediaId;
}
