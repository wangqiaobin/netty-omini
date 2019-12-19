package com.wei.omini.model;

import lombok.Data;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-15 15:59
 */
@Data
public class Context<T> {
    private String req;
    private String cmd;
    private String sub;
    private String version;
    private int state;
    private T content;
}