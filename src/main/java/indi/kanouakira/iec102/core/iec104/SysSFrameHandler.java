package indi.kanouakira.iec102.core.iec104;

import indi.kanouakira.iec102.util.ByteUtil;
import indi.kanouakira.iec102.util.Iec104Util;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.logging.Logger;


public class SysSFrameHandler extends ChannelInboundHandlerAdapter {
	private final Logger logger = Logger.getLogger(SysSFrameHandler.class.getName());

	/**
	 * 拦截系统消息 判断是否S帧
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf result = (ByteBuf) msg;
		byte[] bytes = new byte[result.readableBytes()];
		result.readBytes(bytes);
		if (isSysInstruction(bytes)) {
			logger.info("收到S帧：" + Iec104Util.getAccept(ByteUtil.getByte(bytes, 2, 4)));
			ReferenceCountUtil.release(result);
			return;
		} 
		result.writeBytes(bytes);
		ctx.fireChannelRead(result);
	}

	/**
	 * @Title: isSysInstruction
	 * @param @param bytes
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	private boolean isSysInstruction(byte[] bytes) {
		if (bytes.length != Iec104Constant.APCI_LENGTH) {
			return false;
		}
		if (bytes[Iec104Constant.ACCEPT_LOW_INDEX] == 1 && bytes[Iec104Constant.ACCEPT_HIGH_INDEX] == 0) {
			// 判断S帧的方法
			return true;
		}
		// U帧只有6字节
		return false;
	}
}
