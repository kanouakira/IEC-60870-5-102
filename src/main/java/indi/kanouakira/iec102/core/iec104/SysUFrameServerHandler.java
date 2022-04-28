package indi.kanouakira.iec102.core.iec104;

import indi.kanouakira.iec102.core.iec104.enums.UControlEnum;
import indi.kanouakira.iec102.util.ByteUtil;
import indi.kanouakira.iec102.util.Iec104Util;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Logger;

/**
 * 
* @ClassName: SysUframeInboundHandler
* @Description: 处理U帧的报文 
* @author YDL 
* @date 2020年5月13日
 */
public class SysUFrameServerHandler extends ChannelInboundHandlerAdapter {
	private final Logger logger = Logger.getLogger(SysUFrameClientHandler.class.getName());
	
	/**
	 * 拦截系统消息 
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf result = (ByteBuf) msg;
		byte[] bytes = new byte[result.readableBytes()];
		result.readBytes(bytes);
		if (isSysInstruction(bytes)) {
			UControlEnum uControlEnum = Iec104Util.getUControl(ByteUtil.getByte(bytes, 2, 4));
			if (uControlEnum != null) {
				uInstructionHandler(ctx, result, uControlEnum);
				return;
			}
		} 
		result.writeBytes(bytes);
		ctx.fireChannelRead(result);
	}

	/**
	 * @Title: isSysInstruction
	 * @Description: TODO 判断是否是系统报文
	 * @param @param bytes
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	private boolean isSysInstruction(byte[] bytes) {
		// U帧只有6字节
		return bytes.length == 6;
	}
	
	/**
	 * 
	 * @Title: uInstructionHandler
	 * @Description: 处理U帧
	 * @param ctx
	 * @param result
	 * @param uControlEnum
	 * @return void
	 * @throws
	 */
	private void uInstructionHandler(ChannelHandlerContext ctx, ByteBuf result, UControlEnum uControlEnum) {
		result.readBytes(new byte[result.readableBytes()]);
		byte[] resultBytes = null;
		if (uControlEnum == UControlEnum.TESTFR) {
			logger.info("收到测试指令");
			resultBytes = BasicInstruction104.TESTFR_YES;
		} else if (uControlEnum == UControlEnum.STOPDT) {
			logger.info("收到停止指令");
			resultBytes = BasicInstruction104.STOPDT_YES;
		} else if (uControlEnum == UControlEnum.STARTDT) {
			logger.info("收到启动指令");
			resultBytes = BasicInstruction104.STARTDT_YES;
		} else {
			logger.info("U报文无效" + uControlEnum);
		} 
		if (resultBytes != null) {
			result.writeBytes(resultBytes);
			logger.info("回复U报");
			ctx.writeAndFlush(result);
		} 
	}
}
