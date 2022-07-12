package com.xu1an.common;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2022/07/11/20:16
 * @Description:
 */
public class KeyValue {
    /**
     * key
     */
    private String key;

    /**
     * value
     */
    private String value;


    /**
     * 构造函数。 在调用该方法时，一旦被赋值，就不能改变其值
     * @param key 唯一的key值，
     * @param value 对应的value
     */
    public KeyValue(final String key, final String value) {
        this.key = key;
        this.value = value;
    }


    /**
     * 获得key
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     *  设置key,在调用该方法时，一旦被赋值，就不能改变其值
     * @param key key
     */
    public void setKey(final String key) {
        this.key = key;
    }

    /**
     * 获得value
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置value,在调用该方法时，一旦被赋值，就不能改变其值
     * @param value value
     */
    public void setValue(final String value) {
        this.value = value;
    }
}
