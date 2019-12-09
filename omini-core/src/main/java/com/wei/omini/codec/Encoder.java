package com.wei.omini.codec;

import com.wei.omini.codec.serializer.RemoteSerializer;
import com.wei.omini.codec.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-09-12 13:52
 */
public class Encoder extends MessageToByteEncoder<Object> {

    private static final Serializer serializer = new RemoteSerializer();

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] response = serializer.serialize(msg);
        int length = response.length;
        out.writeInt(length);
        out.writeBytes(response);
    }
}
