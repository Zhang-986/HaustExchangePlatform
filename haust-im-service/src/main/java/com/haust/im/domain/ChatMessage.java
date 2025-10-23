package com.haust.im.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 聊天消息实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 消息类型：JOIN(加入), CHAT(聊天), LEAVE(离开)
     */
    private MessageType type;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 发送者用户名
     */
    private String sender;
    
    /**
     * 接收者用户名（点对点消息）
     */
    private String receiver;
    
    /**
     * 发送时间
     */
    private LocalDateTime timestamp;

    public enum MessageType {
        JOIN,      // 用户加入
        CHAT,      // 聊天消息
        LEAVE,     // 用户离开
        PRIVATE    // 私聊消息
    }
}
