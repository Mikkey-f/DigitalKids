package com.digitalkid.mcp.model.response;

import lombok.Data;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/24 20:31
 */
@Data
public class ChatResponse {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ChatResponse() {
    }

    public ChatResponse(String content) {
        this.content = content;
    }

}
