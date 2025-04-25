package com.digitalkid.mcp.mcp.config;

import org.springframework.ai.autoconfigure.chat.client.ChatClientBuilderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/24 21:23
 */
@Configuration
public class ChatClientConfigurerConfig {
    @Bean
    public ChatClientBuilderConfigurer chatClientBuilderConfigurer() {
        // 这里可以根据需求进行配置
        return new ChatClientBuilderConfigurer();
    }
}
