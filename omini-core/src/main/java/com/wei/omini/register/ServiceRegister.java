package com.wei.omini.register;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-09-12 13:52
 */
public interface ServiceRegister {
    void register(String name, String host, Integer port);

    void shutdown();
}

