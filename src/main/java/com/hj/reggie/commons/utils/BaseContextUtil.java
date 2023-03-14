package com.hj.reggie.commons.utils;

/**
 * id封装到线程中
 * @create 2023-03-09 21:36
 */
public class BaseContextUtil {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
