package com.wei.omini.model;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-09-12 13:52
 */
public interface IServer {
    void start(String name, String host, Integer port) throws InterruptedException;

    void shutdown();
}
