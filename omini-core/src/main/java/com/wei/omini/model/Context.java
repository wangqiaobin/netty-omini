package com.wei.omini.model;

import lombok.Data;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-12-11 10:18
 */
@Data
public class Context {
    private final Long time;
    private Integer state;
    private String handler;
    private RemoteRequest param;
    private RemoteServer server;

    public Context(Long time) {
        this.time = time;
    }
}
