package com.wei.omini.codec;

import com.wei.omini.codec.serializer.RemoteSerializer;
import com.wei.omini.codec.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-09-12 13:52
 */
public class Decoder extends LengthFieldBasedFrameDecoder {

    private static final Serializer serializer = new RemoteSerializer();

    private static final int MAX_FRAME_LENGTH = 10 * 1024 * 1024;

    private static final int LENGTH_FIELD_LENGTH = 4;

    public Decoder() {
        super(MAX_FRAME_LENGTH, 0, LENGTH_FIELD_LENGTH, 0, LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf buf = (ByteBuf) super.decode(ctx, in);
        if (buf != null) {
            int length = buf.readableBytes();
            byte[] bytes = new byte[length];
            buf.readBytes(bytes);
            return serializer.deserialize(bytes);
        }
        return null;
    }
}
