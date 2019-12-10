package com.wei.omini.http;

import com.wei.omini.annotation.Remote;
import com.wei.omini.handler.ServerContextHandler;
import com.wei.omini.model.AbstractRemoteServer;
import com.wei.omini.model.IRemoteServer;
import com.wei.omini.model.RequestContext;
import com.wei.omini.model.ServerInfo;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-12-10 14:42
 */
@Remote(cmd = "http-entry", sub = "router")
public class RemoteHttpHandler extends AbstractRemoteServer implements IRemoteServer {


    @Override
    public int onRequest(ServerInfo server, RequestContext context) {
        return request(server.getName(), context.getCmd(), context.getSub(), context.getState(), context.getContent());
    }

    @Override
    public int onTimeout(ServerInfo server, RequestContext context) {
        ServerContextHandler.Context buffer = ServerContextHandler.getInstance().getContext(context.getReq());
        buffer.getRequest().setContent(null);
        buffer.getRequest().notify();
        return 0;
    }

    @Override
    public int onReceive(ServerInfo server, RequestContext context) {
        ServerContextHandler.Context buffer = ServerContextHandler.getInstance().getContext(context.getReq());
        buffer.getRequest().setContent(context.getContent());
        buffer.getRequest().notify();
        return 0;
    }
}
