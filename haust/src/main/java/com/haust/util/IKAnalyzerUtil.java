package com.haust.util;

import org.springframework.stereotype.Component;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
public class IKAnalyzerUtil {

    /**
     * 使用 IK Analyzer 对文本进行分词
     *
     * @param text 待分词的文本
     * @return 分词结果列表
     */
    public static List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        try (StringReader reader = new StringReader(text)) {
            IKSegmenter segmenter = new IKSegmenter(reader, true); // true 表示启用智能分词模式
            Lexeme lexeme;
            while ((lexeme = segmenter.next()) != null) {
                tokens.add(lexeme.getLexemeText());
            }
        } catch (Exception e) {
            throw new RuntimeException("分词失败", e);
        }
        return tokens;
    }
}