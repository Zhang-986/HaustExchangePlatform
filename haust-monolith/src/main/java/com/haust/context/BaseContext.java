package com.haust.context;

import com.fasterxml.jackson.databind.ser.Serializers;

/*
 用来存储每个用户线程的唯一标识
 */
public class BaseContext {
    // 使用ThreadLocal存储用户信息
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    // 存储对应用户Id
    public static void setId(Long id){
        threadLocal.set(id);
    }
    // 获取对应用户Id
    public static Long getId(){
        return threadLocal.get();
    }
}
