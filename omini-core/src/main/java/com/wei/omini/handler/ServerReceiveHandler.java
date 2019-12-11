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
    protected void channelRead0(ChannelHandlerContext ctx, Context context) {
        log.info("channelRead0 req={}", context);
        IRemoteServer task = ServerContextHandler.getInstance().getRemoteServer(context);
        if (Objects.isNull(task)) {
            //返回调用错误
            return;
        }
        ThreadPoolTaskExecutor executor = ApplicationContextUtil.getBean(ThreadPoolTaskExecutor.class);
        Callable callable;
        Context buffer = ServerContextHandler.getInstance().getContext(context.getParam().getReq());
        if (Objects.nonNull(buffer)) {
            callable = new Callable() {
                @Override
                public Object call() throws Exception {
                    log.info("receive request={}", context);
                    return task.onReceive(context.getServer(), context.getParam());
                }
            };
        } else {
            //2为回包
            if (context.getState().equals(2)) {
                return;
            }
            ServerContextHandler.getInstance().putContext(context);
            callable = new Callable() {
                @Override
                public Object call() throws Exception {
                    log.info("request={}", context);
                    return task.onRequest(context.getServer(), context.getParam());
                }
            };
        }
        executor.submit(callable);
    }
}
