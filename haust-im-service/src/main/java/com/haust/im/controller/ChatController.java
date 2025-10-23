package com.haust.im.controller;

import com.haust.im.domain.ChatMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * IM聊天控制器
 * 处理WebSocket消息
 */
@Slf4j
@Api(tags = "IM即时通讯接口")
@Controller
public class ChatController {

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 处理公共聊天消息
     * 客户端发送到 /app/chat.sendMessage
     * 服务器广播到 /topic/public
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        log.info("收到消息: 发送者={}, 内容={}", chatMessage.getSender(), chatMessage.getContent());
        return chatMessage;
    }

    /**
     * 处理用户加入聊天室
     * 客户端发送到 /app/chat.addUser
     * 服务器广播到 /topic/public
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                                SimpMessageHeaderAccessor headerAccessor) {
        // 在WebSocket会话中添加用户名
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatMessage.setTimestamp(LocalDateTime.now());
        log.info("用户加入聊天室: {}", chatMessage.getSender());
        return chatMessage;
    }

    /**
     * 处理私聊消息
     * 客户端发送到 /app/chat.sendPrivate
     * 服务器发送到特定用户 /user/{username}/queue/private
     */
    @MessageMapping("/chat.sendPrivate")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setType(ChatMessage.MessageType.PRIVATE);
        chatMessage.setTimestamp(LocalDateTime.now());
        log.info("私聊消息: {} -> {}, 内容={}", 
                chatMessage.getSender(), chatMessage.getReceiver(), chatMessage.getContent());
        
        // 发送给接收者
        messagingTemplate.convertAndSendToUser(
                chatMessage.getReceiver(),
                "/queue/private",
                chatMessage
        );
    }
}

/**
 * REST控制器，提供IM服务的REST接口
 */
@RestController
@RequestMapping("/im")
@Api(tags = "IM服务REST接口")
@Slf4j
class IMRestController {
    
    @ApiOperation("健康检查")
    @GetMapping("/health")
    public String health() {
        return "IM Service is running";
    }
}
