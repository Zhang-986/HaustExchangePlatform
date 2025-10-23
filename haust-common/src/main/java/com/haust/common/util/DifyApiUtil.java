package com.haust.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haust.common.properties.ApiProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@EnableConfigurationProperties({ApiProperties.class})
public class DifyApiUtil {

    private final WebClient webClient;
    private final ApiProperties apiProperties;
    private final ObjectMapper objectMapper;

    @Autowired
    public DifyApiUtil(ApiProperties apiProperties, ObjectMapper objectMapper) {
        // 讲解一下配置
        // 1. 设置连接超时时间
        HttpClient httpClient = HttpClient.create()
                // 设置连接池大小
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000000)
                // 设置响应超时时间
                .responseTimeout(Duration.ofSeconds(1000))
                // 设置读取超时时间
                .doOnConnected(
                        conn -> conn
                                // 设置读取超时时间
                        .addHandlerLast(new ReadTimeoutHandler(1000, TimeUnit.SECONDS))
                                // 设置写入超时时间
                        .addHandlerLast(new WriteTimeoutHandler(1000    , TimeUnit.SECONDS)));

        this.apiProperties = apiProperties;
        this.objectMapper = objectMapper;
        this.webClient = WebClient.builder()
                // 设置连接池大小
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                // 设置请求头
                .baseUrl(apiProperties.getUrl())
                .defaultHeader("Authorization", "Bearer " + apiProperties.getApiKey())
                .build();
    }

    public Flux<String> streamChat(String prompt, String userId, String conversationId) throws DifyApiException {
        validateInputs(prompt, userId);

        Map<String, Object> requestBody = buildRequestBody(prompt, userId, conversationId);

        log.info("Sending request to Dify API: {}", requestBody);

        return webClient.post()
                .uri(apiProperties.getChatEndpoint())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                // 记录事件，但不打印完整内容，避免日志截断
                .doOnNext(rawEvent -> {
                    if (rawEvent != null && rawEvent.length() > 50) {
                        log.debug("Event received, length: {}, preview: {}",
                                rawEvent.length(), rawEvent.substring(0, 50) + "...");
                    } else {
                        log.debug("Event received: {}", rawEvent);
                    }
                })
                .filter(event -> event != null)
                .map(event -> {
                    try {
                        // 尝试直接解析JSON，不考虑SSE格式
                        Map<String, Object> eventMap = objectMapper.readValue(event, Map.class);
                        String eventType = (String) eventMap.get("event");

                        // 只提取agent_message事件中的answer字段
                        if ("agent_message".equals(eventType)) {
                            Object answer = eventMap.get("answer");
                            if (answer != null) {
                                return answer.toString();
                            }
                        }
                        // 也可以从message事件提取
                        else if ("message".equals(eventType)) {
                            Object answer = eventMap.get("answer");
                            if (answer != null) {
                                return answer.toString();
                            }
                        }

                        // 忽略其他类型事件
                        return "";

                    } catch (Exception e) {
                        log.warn("Failed to parse event: {} - {}",
                                event.length() > 100 ? event.substring(0, 100) + "..." : event,
                                e.getMessage());
                        return "";
                    }
                })
                .filter(content -> !content.isEmpty())
                .doOnError(error -> {
                    log.error("Error in SSE stream: ", error);
                });
    }

    private void validateInputs(String prompt, String userId) throws DifyApiException {
        if (apiProperties.getApiKey() == null || apiProperties.getApiKey().isEmpty()) {
            log.error("DIFY API Key is not configured.");
            throw new DifyApiException("DIFY API Key is missing.");
        }
        if (prompt == null || prompt.trim().isEmpty()) {
            log.error("Prompt cannot be empty.");
            throw new DifyApiException("Prompt cannot be empty.");
        }
    }

    private Map<String, Object> buildRequestBody(String prompt, String userId, String conversationId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", Collections.emptyMap());
        requestBody.put("query", prompt);
        requestBody.put("response_mode", "streaming");
        requestBody.put("user", userId != null ? userId : "unknown-user");

        if (conversationId != null && !conversationId.isEmpty()) {
            requestBody.put("conversation_id", conversationId);
        }

        return requestBody;
    }

    public static class DifyApiException extends Exception {
        public DifyApiException(String message) {
            super(message);
        }

        public DifyApiException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}