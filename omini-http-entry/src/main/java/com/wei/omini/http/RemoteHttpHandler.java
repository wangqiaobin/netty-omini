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
    public int onRequest(RemoteServer server, Context context) {
        return request(server.getName(), context.getCmd(), context.getSub(), context.getState(), context);
    }

    @Override
    public int onTimeout(RemoteServer server, Context context) {
        InnerContext inner = ServerContextHandler.getInstance().getContext(context.getReq());
        inner.setContext(null);
        synchronized (inner.getTime()) {
            inner.getTime().notify();
        }
        return 0;
    }

    @Override
    public int onReceive(RemoteServer server, Context context) {
        InnerContext inner = ServerContextHandler.getInstance().getContext(context.getReq());
        inner.setContext(context);
        synchronized (inner.getTime()) {
            inner.getTime().notify();
        }
        return 0;
    }
}
