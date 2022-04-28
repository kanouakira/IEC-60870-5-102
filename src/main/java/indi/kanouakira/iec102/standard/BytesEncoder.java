package indi.kanouakira.iec102.standard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 数组编码器。
 *
 * @author KanouAkira
 * @date 2022/4/18 20:52
 */
public class BytesEncoder extends MessageToByteEncoder<byte[]> {

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) {
        out.writeBytes(msg);
    }

}
