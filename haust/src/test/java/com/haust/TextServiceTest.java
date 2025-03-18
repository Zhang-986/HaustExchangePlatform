package com.haust;

import com.haust.util.IKAnalyzerUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TextServiceTest {


    @Test
    public void testTokenize() {
        String text = "这是一个中文分词测试";
        List<String> tokens = IKAnalyzerUtil.tokenize(text);
        System.out.println("分词结果: " + tokens);
        // 输出: [这是, 一个, 中文, 分词, 测试]
    }
}