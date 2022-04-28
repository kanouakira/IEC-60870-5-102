package indi.kanouakira.iec102.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author YDL
 * @ClassName: Unpack104Util
 * @Description: 解决TCP 拆包和沾包的问题
 * @date 2020年5月13日
 */
public class Iec102UnpackHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        // 记录包头开始的 index
        int beginReader = 0;
        // 记录包结束的 index
        int endReader = 0;
        boolean findEnd = false;
        while (true) {
            if (findEnd) {
                endReader = buffer.readerIndex();
            } else {
                // 获取包头开始的 index
                beginReader = buffer.readerIndex();
            }
            // 记录一个标志用于重置
            buffer.markReaderIndex();
            // 读到了102协议的开始标志
            byte readByte = buffer.readByte();
            if (readByte == Iec102Constant.FIXED_HEAD_DATA || readByte == Iec102Constant.VARIABLE_HEAD_DATA) {
                // 开始寻找结束标志
                findEnd = true;
            }
            // 读到102协议的结束标志，结束 while 循环
            if (readByte == Iec102Constant.END_DATA) {
                break;
            }
            continue;
        }
        // 恢复指针
        buffer.readerIndex(beginReader);
        int length = endReader - beginReader;
        ByteBuf data = buffer.readBytes(length + 1);
        out.add(data);
    }

}
