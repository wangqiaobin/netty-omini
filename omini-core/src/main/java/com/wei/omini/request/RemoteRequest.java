package com.wei.omini.request;

import com.wei.omini.common.util.IPv4Util;
import com.wei.omini.common.util.IdUtil;
import com.wei.omini.configuration.RemoteProperties;
import com.wei.omini.constants.Constants;
import com.wei.omini.exception.NotFoundRegistryException;
import com.wei.omini.handler.ServerContextHandler;
import com.wei.omini.model.Client;
import com.wei.omini.model.Context;
import com.wei.omini.model.RemoteServer;
import com.wei.omini.register.ServiceDiscover;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-15 16:30
 */
@Slf4j
@Component
public class RemoteRequest {

    @Resource
    private ServiceDiscover discover;

    @Resource
    private RemoteProperties properties;

    private final static String host = IPv4Util.getAddressIp();


    public <T extends com.wei.omini.model.RemoteRequest> int request(String name, T data) {
        Client client = getRemoteClient(name);
        if (StringUtil.isNullOrEmpty(data.getVersion())) {
            data.setVersion(Constants.DEFAULT_VERSION);
        }
        RemoteServer server = new RemoteServer(name, host, properties.getPort());
        Context context = new Context(System.currentTimeMillis());
        context.setParam(data);
        context.setServer(server);
        context.setState(1);
        context.setHandler(ServerContextHandler.getInstance().getLocal().get());
        if (StringUtil.isNullOrEmpty(data.getReq())) {
            data.setReq(IdUtil.buildHax(System.currentTimeMillis()));
        }
        client.getChannel().writeAndFlush(context);
        return 0;
    }

    public <T extends com.wei.omini.model.RemoteRequest> int receive(String name, String host, Integer port, T data) {
        Client client = getRemoteClient(name, host, port);
        Context context = ServerContextHandler.getInstance().getContext(data.getReq());
        context.setState(2);
        context.setParam(data);
        client.getChannel().writeAndFlush(context);
        return 0;
    }

    private Client getRemoteClient(String name) {
        Client client = this.discover.discover(name);
        if (Objects.isNull(client)) {
            throw new NotFoundRegistryException();
        }
        return client;
    }

    private Client getRemoteClient(String name, String host, Integer port) {
        Client client = this.discover.discover(name, host, port);
        if (Objects.isNull(client)) {
            throw new NotFoundRegistryException();
        }
        return client;
    }

}