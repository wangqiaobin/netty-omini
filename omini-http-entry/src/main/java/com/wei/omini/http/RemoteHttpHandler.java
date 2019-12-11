package com.wei.omini.http;

import com.wei.omini.annotation.Remote;
import com.wei.omini.handler.ServerContextHandler;
import com.wei.omini.model.*;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-12-10 14:42
 */
@Remote(cmd = "http-entry", sub = "router")
public class RemoteHttpHandler extends AbstractRemoteServer implements IRemoteServer {
    @Override
    public int onRequest(RemoteServer server, RemoteRequest param) {
        return request(server.getName(), param.getCmd(), param.getSub(), param.getState(), param);
    }

    @Override
    public int onTimeout(RemoteServer server, RemoteRequest param) {
        Context context = ServerContextHandler.getInstance().getContext(param.getReq());
        context.setParam(null);
        synchronized (context.getTime()) {
            context.getTime().notify();
        }
        return 0;
    }

    @Override
    public int onReceive(RemoteServer server, RemoteRequest param) {
        Context context = ServerContextHandler.getInstance().getContext(param.getReq());
        context.setParam(param);
        synchronized (context.getTime()) {
            context.getTime().notify();
        }
        return 0;
    }
}
