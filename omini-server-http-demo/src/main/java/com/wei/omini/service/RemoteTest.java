package com.wei.omini.service;

import com.wei.omini.annotation.Remote;
import com.wei.omini.model.AbstractRemoteServer;
import com.wei.omini.model.IRemoteServer;
import com.wei.omini.model.RequestContext;
import com.wei.omini.model.ServerInfo;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-12-10 19:40
 */
@Remote(cmd = "test", sub = "test")
public class RemoteTest extends AbstractRemoteServer implements IRemoteServer {
    @Override
    public int onRequest(ServerInfo server, RequestContext context) {
        context.setContent("111");
        return response(server, context);
    }

    @Override
    public int onTimeout(ServerInfo server, RequestContext context) {
        return 0;
    }

    @Override
    public int onReceive(ServerInfo server, RequestContext context) {
        return 0;
    }
}
