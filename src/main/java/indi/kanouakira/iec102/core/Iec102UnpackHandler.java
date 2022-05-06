package indi.kanouakira.iec102.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static indi.kanouakira.iec102.core.Iec102Constant.FRAME_LENGTH;
import static indi.kanouakira.iec102.util.ByteUtil.byteArrayToShort;
import static indi.kanouakira.iec102.util.ByteUtil.reverse;

/**
 * 解决 TCP 拆包和沾包的问题。
 * @author KanouAkira
 * @date 2022/4/22 16:44
 */
public class Iec102UnpackHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        // 记录包头开始的 index
        int beginReader = 0;
        int newDataLength = 0;
        boolean readComplete = false;
        // 记录已读长度
        int offsetLength = 0;
        while (!readComplete) {
            // 获取包头开始的 index
            beginReader = buffer.readerIndex();
            // 记录一个标志用于重置
            buffer.markReaderIndex();
            // 读到了102协议的开始标志
            byte readByte = buffer.readByte();
            offsetLength = 1;
            switch (readByte){
                case Iec102Constant.FIXED_HEAD_DATA -> {
                    newDataLength = 5;
                    readComplete = true;
                }
                case Iec102Constant.VARIABLE_HEAD_DATA -> {
                    // 标记当前包为新包
                    // 读取包长度,两个字节
                    byte[] newDataLengthByte = new byte[FRAME_LENGTH];
                    offsetLength += FRAME_LENGTH;
                    buffer.readBytes(newDataLengthByte);
                    newDataLength = byteArrayToShort(reverse(newDataLengthByte));
                    readComplete = true;
                    // 102 中帧长只包括校验部分长度， 所以还需要加上一个起始字符和校验和还有结束字符。
                    offsetLength += 3;
                }
            }
        }

        if (buffer.readableBytes() < newDataLength) {
            buffer.readerIndex(beginReader);
            return;
        }

        newDataLength = newDataLength + offsetLength;
        // 恢复指针
        buffer.readerIndex(beginReader);
        ByteBuf data = buffer.readBytes(newDataLength);
        out.add(data);
    }

}
