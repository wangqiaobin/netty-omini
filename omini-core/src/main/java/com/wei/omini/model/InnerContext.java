package com.wei.omini.model;

import lombok.Data;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-12-11 10:18
 */
@Data
public class InnerContext {
    private final Long time;
    private Integer state;
    private String handler;
    private Context param;
    private RemoteServer server;

    public InnerContext(Long time) {
        this.time = time;
    }
}
