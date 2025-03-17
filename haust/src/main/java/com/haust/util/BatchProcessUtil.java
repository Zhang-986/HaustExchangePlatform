package com.haust.util;

import com.haust.configuration.ThreadConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Component
@RequiredArgsConstructor
public class BatchProcessUtil {
    private final ThreadPoolExecutor executor;
    public void process(){

    }
}
