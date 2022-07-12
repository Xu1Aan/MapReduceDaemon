package com.xu1an.handler;

import com.xu1an.common.KeyValue;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2022/07/11/20:15
 * @Description:
 */
public interface MapReduce {
    List<KeyValue> map(String contents);

    String reduce(List<String> values);
}
