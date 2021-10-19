package com.key.win.client.dto;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 路由过滤器模型
 */
@EqualsAndHashCode
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class GatewayFilterDefinition {
    private String name;
    @Builder.Default
    private Map<String, String> args = new LinkedHashMap<>();
}
