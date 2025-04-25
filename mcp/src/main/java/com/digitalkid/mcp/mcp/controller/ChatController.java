package com.digitalkid.mcp.controller;

import com.digitalkid.mcp.model.request.ChatRequest;
import com.digitalkid.mcp.model.response.ChatResponse;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/24 21:24
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Resource
    private ChatClient chatClient;

    /**
     * 聊天请求
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        try {
            // 创建用户消息
            String userMessage = request.getMessage();

            // 使用流式API调用聊天
            String content = chatClient.prompt()
                    .user(userMessage)
                    .call()
                    .content();

            return ResponseEntity.ok(new ChatResponse(content));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ChatResponse("处理请求时出错: " + e.getMessage()));
        }
    }
}
