package com.wjicloud.simpson.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回前端的结果类
 * @param <T>
 */
@Data
public class R<T> {
    private Integer code;
    private String msg;
    private T data;
    private Map map = new HashMap();
    public static <T> R<T> success(T object){
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }
    public static <T> R<T> error(String msg){
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }
    public R<T> add(String key,Object value){
        this.map.put(key,value);
        return this;
    }
}
