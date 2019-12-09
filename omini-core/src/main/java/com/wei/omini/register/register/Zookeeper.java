package com.wei.omini.register.register;

import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-09-19 23:47
 */
public interface Zookeeper {

    void connection(String address, Integer timeout, Watcher watcher);

    void close();

    boolean createPath(String path);

    String writeData(String path, String data);

    String readData(String path);

    void deleteNode(String path);

    boolean exists(String path);

    List<String> getChildren(String path);

    void deleteAllPath();
}
