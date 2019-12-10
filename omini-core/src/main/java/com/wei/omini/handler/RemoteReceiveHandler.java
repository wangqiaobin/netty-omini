package com.wei.omini.handler;

import com.wei.omini.annotation.Remote;
import com.wei.omini.exception.DuplicateRemoteException;
import com.wei.omini.model.IRemoteServer;
import com.wei.omini.model.RequestContext;
import com.wei.omini.model.ServerInfo;
import com.wei.omini.util.ApplicationContextUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-09-12 13:52
 */
@Slf4j
public class RemoteReceiveHandler extends SimpleChannelInboundHandler<RequestContext> {

    public RemoteReceiveHandler() throws DuplicateRemoteException {
        Map<String, IRemoteServer> beans = ApplicationContextUtil.getBeansOfType(IRemoteServer.class);
        for (Map.Entry<String, IRemoteServer> entry : beans.entrySet()) {
            Remote annotation = entry.getValue().getClass().getAnnotation(Remote.class);
            if (annotation == null || StringUtil.isNullOrEmpty(annotation.cmd()) || StringUtil.isNullOrEmpty(annotation.sub())) {
                continue;
            }
            if (ServerContextHandler.getInstance().hasRemoteServer(annotation.cmd(), annotation.sub(), annotation.version())) {
                continue;
            }
            ServerContextHandler.getInstance().putRemoteServer(annotation.cmd(), annotation.sub(), annotation.version(), entry.getValue());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestContext request) {
        IRemoteServer task = ServerContextHandler.getInstance().getRemoteServer(request.getCmd(), request.getSub(), request.getVersion());
        if (Objects.isNull(task)) {
            //返回调用错误
            return;
        }
        ThreadPoolTaskExecutor executor = ApplicationContextUtil.getBean(ThreadPoolTaskExecutor.class);
        Callable callable;
        ServerContextHandler.Context context = ServerContextHandler.getInstance().getContext(request.getReq());
        if (Objects.nonNull(context)) {
            callable = new Callable() {
                @Override
                public Object call() throws Exception {
                    log.info("receive request={}", request);
                    return task.onReceive(context.getServer(), request);
                }
            };
        } else {
            if (request.isAck()) {
                return;
            }
            InetSocketAddress address = (InetSocketAddress) ctx.channel().localAddress();
            request.setTime(System.currentTimeMillis());
            ServerInfo server = new ServerInfo();
            server.setHost(address.getHostString());
            server.setPort(address.getPort());
            server.setName(request.getFrom());
            ServerContextHandler.getInstance().putContext(server, request);
            callable = new Callable() {
                @Override
                public Object call() throws Exception {
                    log.info("request request={}", request);
                    return task.onRequest(server, request);
                }
            };
        }
        executor.submit(callable);
    }
}
