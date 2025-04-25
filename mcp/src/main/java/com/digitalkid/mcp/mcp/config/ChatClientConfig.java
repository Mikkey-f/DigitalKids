package com.digitalkid.mcp.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/24 21:19
 */
@Configuration
public class ChatClientConfig {

    @Autowired
    private ToolCallbackProvider toolCallbackProvider;

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClient) {
        return chatClient
                .defaultSystem("你是一个百科文章管理助手，开源帮助用户查询百科文章信息。" +
                        "你可以根据百科文章标题模糊查询，根据阶段等分类查询" +
                        "回复时，请使用简洁友好的语言，并将图书信息整理为易读的格式。")
                .defaultTools(toolCallbackProvider)
                .build();
    }
}
