package com.haust.gateway.filter;

import jdk.nashorn.internal.parser.Token;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
@Order(-1)
@Component
@Slf4j
public class GlobalAuthFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if(request !=null){
            HttpHeaders headers = request.getHeaders();
            List<String> strings = headers.get("Authorization");
            if(CollectionUtils.isNotEmpty(strings)){
                String s = strings.get(0);
                // 鉴权处理


            }
        }
        return null;
    }
}
