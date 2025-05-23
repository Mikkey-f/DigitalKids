package com.digital.model.vo.guest;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-13  18:34
 */
@Data
@AllArgsConstructor
public class LoginGuestVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String guestName;

    private String token;
}
