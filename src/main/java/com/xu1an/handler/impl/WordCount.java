package com.xu1an.handler.impl;

import com.xu1an.common.KeyValue;
import com.xu1an.handler.MapReduce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2022/07/11/20:16
 * @Description:
 */
public class WordCount implements MapReduce {
    @Override
    public List<KeyValue> map(String contents) {
        // Splitting 分割
        List<String> words = Arrays.stream(contents.split("[^a-zA-Z]+"))
                .filter(word -> !"".equals(word))
                .collect(Collectors.toList());

        // Mapping
        List<KeyValue> keyValues = new ArrayList<>(words.size());
        for (String word : words) {
            KeyValue keyValue = new KeyValue(word, "1");
            keyValues.add(keyValue);
        }
        return keyValues;
    }

    @Override
    public String reduce(List<String> values) {

        // Reducing
        return String.valueOf(values.size());
    }
}
