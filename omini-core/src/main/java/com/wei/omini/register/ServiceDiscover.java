package com.wei.omini.register;

import com.wei.omini.model.Client;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-09-12 13:52
 */
public interface ServiceDiscover {

    /**
     * 服务发现
     *
     * @param name
     * @return
     */
    Client discover(String name);

    Client discover(String name, String host, Integer port);
}
