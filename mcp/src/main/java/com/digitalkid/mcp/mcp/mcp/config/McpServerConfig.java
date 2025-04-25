package com.digitalkid.mcp.mcp.config;

import com.digitalkid.mcp.service.ParentingEncyclopediaService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/24 21:26
 */
@Configuration
public class McpServerConfig {
    /**
     * 注册工具回调提供者，将BookQueryService中的@Tool方法暴露为MCP工具
     *
     * @param parentingEncyclopediaService 百科服务
     * @return 工具回调提供者
     */
    @Bean
    public ToolCallbackProvider parentingEncyclopediaProvider(ParentingEncyclopediaService parentingEncyclopediaService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(parentingEncyclopediaService)
                .build();
    }
}
