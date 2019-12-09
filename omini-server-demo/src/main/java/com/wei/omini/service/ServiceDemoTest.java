package com.wei.omini.service;

import com.wei.omini.annotation.Remote;
import com.wei.omini.model.AbstractRemoteServer;
import com.wei.omini.model.IRemoteServer;
import com.wei.omini.model.RequestContext;
import com.wei.omini.model.ServerInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-21 16:53
 */
@Slf4j
@Remote(cmd = "ent", sub = "insert")
public class ServiceDemoTest extends AbstractRemoteServer implements IRemoteServer {
    @Override
    public int onRequest(ServerInfo server, RequestContext context) {
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
