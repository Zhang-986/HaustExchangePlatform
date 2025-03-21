package com.haust.util;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
/**
 * 布隆过滤器相关工具
 */
@Service
public class BloomFilterUtil {
    private final RedissonClient redissonClient;
    private String sensitiveWordsFilePath ="haust/src/main/resources/SensitiveWords.txt";
    private final String filterName = "sensitive_words_filter";

    public BloomFilterUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        // 初始化布隆过滤器
        initBloomFilter();
    }

    private void initBloomFilter() {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(filterName);
        // 初始化布隆过滤器：预计元素数量为 10000，误判率为 1%
        bloomFilter.tryInit(10000L, 0.01);
        // 加载敏感词并添加到布隆过滤器中
        // loadSensitiveWords(bloomFilter);
    }
    /**
     * 从文件加载敏感词并添加到布隆过滤器中
     *
     * @param bloomFilter 布隆过滤器实例
     */
    private void loadSensitiveWords(RBloomFilter<String> bloomFilter) {
        try (BufferedReader reader = new BufferedReader(new FileReader(sensitiveWordsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bloomFilter.add(line.trim()); // 添加敏感词到布隆过滤器
            }
            System.out.println("布隆过滤器初始化完成，已加载敏感词。");
        } catch (IOException e) {
            throw new RuntimeException("加载敏感词文件失败: " + sensitiveWordsFilePath, e);
        }
    }

    /**
     * 添加对应敏感词到布隆过滤器当中
     * @param item
     */
    public void addItem(String item) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(filterName);
        bloomFilter.add(item);
    }
    /**
     * 判断敏感词是否存在于布隆过滤器中
     * @param item
     */

    public boolean existsItem(String item) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(filterName);
        return bloomFilter.contains(item);
    }

}