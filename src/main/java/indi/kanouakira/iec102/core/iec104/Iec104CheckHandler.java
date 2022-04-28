package indi.kanouakira.iec102.core.iec104;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 
 * @ClassName: Check104Handler
 * @Description: 检查104报文
 * @author YDL
 * @date 2020年5月13日
 */
public class Iec104CheckHandler extends ChannelInboundHandlerAdapter {
	
	/**
	 * 拦截系统消息
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf result = (ByteBuf) msg;
		byte[] bytes = new byte[result.readableBytes()];
		result.readBytes(bytes);
		if (bytes.length < Iec104Constant.APCI_LENGTH || bytes[0] != Iec104Constant.HEAD_DATA) {
			ReferenceCountUtil.release(result);
		} else {
			result.writeBytes(bytes);
			ctx.fireChannelRead(msg);
		}
	}
}
