package indi.kanouakira.iec102.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import static indi.kanouakira.iec102.core.Iec102Constant.*;
import static indi.kanouakira.iec102.util.ByteUtil.byteArrayToHexString;
import static indi.kanouakira.iec102.util.Iec102Util.calcCrc8;
import static java.util.Arrays.copyOfRange;

/**
 * 校验报文是否符合 IEC 60870-5-102 规范
 * @author KanouAkira
 * @date 2022/4/20 15:37
 */
public class Iec102CheckHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf result = (ByteBuf) msg;
        byte[] bytes = new byte[result.readableBytes()];
        result.readBytes(bytes);
        boolean illegalFrame = true;
        if (bytes.length == 1){
            // TODO: 2022/4/20 单字符报文处理
        }else {
            byte startByte = bytes[0];
            int crcPos = bytes.length - 2;
            if (startByte == VARIABLE_HEAD_DATA){ // 可变帧长报文
                if (bytes[crcPos] == calcCrc8(copyOfRange(bytes, VARIABLE_CHECK_START_POS, crcPos))){
                    illegalFrame = false;
                }
            }else if (startByte == FIXED_HEAD_DATA){ // 固定帧长报文
                if (bytes[crcPos] == calcCrc8(copyOfRange(bytes, FIXED_CHECK_START_POS, crcPos))){
                    illegalFrame = false;
                }
            }
        }

        if (illegalFrame) {
            // 不合法释放ByteBuf
            ReferenceCountUtil.release(result);
        } else {
            System.out.println(byteArrayToHexString(bytes));
            result.writeBytes(bytes);
            ctx.fireChannelRead(msg);
        }
    }

}
