package com.wei.omini.demo;

import com.wei.omini.annotation.Remote;
import com.wei.omini.model.AbstractRemoteServer;
import com.wei.omini.model.IRemoteServer;
import com.wei.omini.model.RemoteParam;
import com.wei.omini.model.RemoteServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-12-11 10:02
 */
@Slf4j
@Remote(cmd = "test", sub = "test")
public class DemoTest extends AbstractRemoteServer implements IRemoteServer {
    @Override
    public int onRequest(RemoteServer server, RemoteParam param) {
        log.info("on_request context={} info={}", param, server);
        param.setContent("1123123123");
        return response(server, param);
    }

    @Override
    public int onTimeout(RemoteServer server, RemoteParam param) {
        return 0;
    }

    @Override
    public int onReceive(RemoteServer server, RemoteParam param) {
        return 0;
    }
}
