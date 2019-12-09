package com.wei.omini.model;

import com.wei.omini.codec.Decoder;
import com.wei.omini.codec.Encoder;
import com.wei.omini.handler.RemoteReceiveHandler;
import com.wei.omini.register.ServiceRegister;
import com.wei.omini.util.ApplicationContextUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-18 16:12
 */
@Slf4j
public class RemoteServer implements IServer {
    // handle socket accept
    private EventLoopGroup boss = new NioEventLoopGroup(5);
    // handle read and write event
    private EventLoopGroup work = new NioEventLoopGroup(10);

    private ServiceRegister register;


    public RemoteServer() {
        register = ApplicationContextUtil.getBean(ServiceRegister.class);
    }

    @Override
    public void start(String name, String host, Integer port) throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new Decoder())
                                .addLast(new Encoder())
                                .addLast(new RemoteReceiveHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture future = bootstrap.bind(host, port).sync();
        register.register(name, host, port);
        log.info("start server on port <{}>", port);
        future.channel().closeFuture().sync();
    }

    @Override
    public void shutdown() {
        work.shutdownGracefully();
        boss.shutdownGracefully();
        register.shutdown();
    }
}
