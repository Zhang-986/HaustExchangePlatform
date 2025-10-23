package com.haust.im.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket配置类
 * 使用STOMP协议进行消息传输
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 启用简 单的内存消息代理，用于将消息发送回客户端
        config.enableSimpleBroker("/topic", "/queue");
        // 设置应用程序目标前缀，客户端发送消息的目标前缀
        config.setApplicationDestinationPrefixes("/app");
        // 设置用户目标前缀，用于点对点消息
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册STOMP端点，客户端通过此端点连接WebSocket
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
