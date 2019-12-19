package com.wei.omini.model;

import com.wei.omini.annotation.Remote;
import com.wei.omini.handler.ServerContextHandler;
import com.wei.omini.request.RequestClient;

import javax.annotation.Resource;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-21 22:45
 */
public abstract class AbstractRemoteServer implements IRemoteServer {

    @Resource
    private RequestClient client;

    protected <T> int request(String name, String cmd, String sub, Integer state, T data) {
        Remote annotation = getClass().getAnnotation(Remote.class);
        ServerContextHandler.getInstance().getLocal().set(ServerContextHandler.getInstance().buildHandlerKey(annotation.cmd(), annotation.sub(), annotation.version()));
        return client.request(name, (Context) data);
    }

    protected <T extends Context> int response(RemoteServer server, T data) {
        return client.receive(server.getName(), server.getHost(), server.getPort(), data);
    }
}
