package com.digital.model.request.mcp;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/24 20:30
 */
@Data
public class ChatRequest implements Serializable {

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ChatRequest() {
    }

    public ChatRequest(String message) {
        this.message = message;
    }

}