package com.wei.omini.model;

import com.wei.omini.codec.Encoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-21 15:47
 */
@Slf4j
public class RemoteClient {
    private EventLoopGroup group = new NioEventLoopGroup();

    @Getter
    private RemoteServer server = new RemoteServer();
    @Getter
    private Channel channel;

    public RemoteClient(String name, String host, int port) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new Encoder());
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true);
        try {
            server.setName(name);
            server.setHost(host);
            server.setPort(port);
            channel = bootstrap.connect(host, port).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
