package com.haust.user.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component; // 建议添加 @Component

 // 让Spring扫描并创建这个Bean
@ConfigurationProperties(prefix = "dify.api") // 定义属性前缀
public class ApiProperties {

    private String apiKey;
    private String url = "http://192.168.150.107/v1"; // Dify API 基础 URL
    private String chatEndpoint = "/chat-messages"; // Chat 端点

    // Getters and Setters
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChatEndpoint() {
        return chatEndpoint;
    }

    public void setChatEndpoint(String chatEndpoint) {
        this.chatEndpoint = chatEndpoint;
    }
}