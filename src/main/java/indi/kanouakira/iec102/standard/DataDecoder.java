package indi.kanouakira.iec102.standard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器，将byte数组转换成信息帧。
 * @author KanouAkira
 * @date 2022/4/18 21:20
 */
public class DataDecoder extends ByteToMessageDecoder {

    Decoder decoder;

    public DataDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        MessageDetail messageDetail = decoder.decode(data);
        out.add(messageDetail);
    }

}
