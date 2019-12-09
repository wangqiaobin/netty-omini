package com.wei.omini.model;

import com.wei.omini.request.RemoteRequest;

import javax.annotation.Resource;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-21 22:45
 */
public abstract class AbstractRemoteServer implements IRemoteServer {

    @Resource
    private RemoteRequest request;

    protected <T> int request(String name, String cmd, String sub, Integer state, T data) {
        RequestContext context = new RequestContext();
        context.setState(state);
        context.setCmd(cmd);
        context.setSub(sub);
        context.setContent(data);
        return request.request(name, context);
    }

    protected <T extends RequestContext> int response(ServerInfo info, T data) {
        return request.receive(info.getName(), info.getHost(), info.getPort(), data);
    }
}
