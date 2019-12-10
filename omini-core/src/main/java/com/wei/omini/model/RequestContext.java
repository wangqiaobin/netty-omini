package com.wei.omini.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-15 15:59
 */
@Data
public class RequestContext {
    private String from;
    private String req;
    private String cmd;
    private String sub;
    private String version;
    private int state;
    private long time;
    @Getter
    private boolean ack;
    @Setter
    private Object content;

    public <T> T getContent() {
        return (T) content;
    }
}