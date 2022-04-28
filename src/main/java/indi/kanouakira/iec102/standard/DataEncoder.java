package indi.kanouakira.iec102.standard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器。
 *
 * @author KanouAkira
 * @date 2022/4/18 21:20
 */
public class DataEncoder extends MessageToByteEncoder<MessageDetail> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageDetail msg, ByteBuf out) throws Exception {
        out.writeBytes(msg.encode());
    }

}
