package com.wei.omini.common.util;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-19 11:05
 */
public class ThreadUtil {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
