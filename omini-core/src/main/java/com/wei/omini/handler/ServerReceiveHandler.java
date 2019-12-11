package com.wei.omini.handler;

import com.wei.omini.annotation.Remote;
import com.wei.omini.exception.DuplicateRemoteException;
import com.wei.omini.model.Context;
import com.wei.omini.model.IRemoteServer;
import com.wei.omini.util.ApplicationContextUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-09-12 13:52
 */
@Slf4j
public class ServerReceiveHandler extends SimpleChannelInboundHandler<Context> {

    public ServerReceiveHandler() throws DuplicateRemoteException {
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
    protected void channelRead0(ChannelHandlerContext ctx, Context request) {
        IRemoteServer task = ServerContextHandler.getInstance().getRemoteServer(request.getParam().getCmd(), request.getParam().getSub(), request.getParam().getVersion());
        if (Objects.isNull(task)) {
            //返回调用错误
            return;
        }
        ThreadPoolTaskExecutor executor = ApplicationContextUtil.getBean(ThreadPoolTaskExecutor.class);
        Callable callable;
        Context context = ServerContextHandler.getInstance().getContext(request.getParam().getReq());
        if (Objects.nonNull(context)) {
            callable = new Callable() {
                @Override
                public Object call() throws Exception {
                    log.info("receive request={}", request);
                    return task.onReceive(context.getServer(), request.getParam());
                }
            };
        } else {
            //2为回包
            if (request.getState().equals(2)) {
                return;
            }
            request.setTime(System.currentTimeMillis());
            ServerContextHandler.getInstance().putContext(request);
            callable = new Callable() {
                @Override
                public Object call() throws Exception {
                    log.info("request request={}", request);
                    return task.onRequest(request.getServer(), request.getParam());
                }
            };
        }
        executor.submit(callable);
    }
}
