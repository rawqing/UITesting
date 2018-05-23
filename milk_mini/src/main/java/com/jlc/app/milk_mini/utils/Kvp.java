package com.jlc.app.milk_mini.utils;


import java.util.Collection;
import java.util.Map;

/**
 * Created by king on 2017/6/21.
 * 键值对的模板
 */

public class Kvp<K,V> {
    private K key;
    private V value;

    public Kvp() {
    }

    public Kvp(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public boolean isEmpty(){
        if (value instanceof String) {
            return ((String) value).isEmpty();
        }
        if (value instanceof Collection) {
            return ((Collection) value).isEmpty();
        }
        if (value instanceof Map) {
            return ((Map) value).isEmpty();
        }
        return value == null;
    }
//    @Override
//    public String toString(){
//        return JSON.toJSONString(this);
//    }
}
