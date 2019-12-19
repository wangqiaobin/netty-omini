package com.wei.omini.util;

import com.wei.omini.model.Context;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-12-19 09:45
 */
public class ContextBuild<T> {

    private Context<T> context = new Context();

    public ContextBuild setReq(String req) {
        context.setReq(req);
        return this;
    }

    public ContextBuild setCmd(String cmd) {
        context.setCmd(cmd);
        return this;
    }

    public ContextBuild setSub(String sub) {
        context.setSub(sub);
        return this;
    }

    public ContextBuild setVersion(String version) {
        context.setVersion(version);
        return this;
    }

    public ContextBuild setState(int state) {
        context.setState(state);
        return this;
    }

    public ContextBuild setContent(T content) {
        context.setContent(content);
        return this;
    }
}
