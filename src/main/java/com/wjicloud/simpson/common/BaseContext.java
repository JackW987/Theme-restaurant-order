package com.wjicloud.simpson.common;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程携带的值
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取当前线程携带的值
     * @return
     */
    public static Long getCurrentId(){
        Long currentId = threadLocal.get();
        return currentId;
    }
}
