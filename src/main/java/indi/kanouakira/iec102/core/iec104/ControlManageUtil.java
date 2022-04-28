package indi.kanouakira.iec102.core.iec104;

import indi.kanouakira.iec102.standard.MessageDetail;
import indi.kanouakira.iec102.util.Iec104Util;
import io.netty.channel.ChannelHandlerContext;

/**
 * 控制域的管理工具
 */
public class ControlManageUtil {

	/**
	 * 发送序号
	 */
	private Short send;

	private final Object sendLock;

	/**
	 * 接收序号
	 */
	private Short accept;

	/**
	 * 接收到帧的数量
	 */
	private Short frameAmount;

	/**
	 * 发送S帧的锁
	 */
	private final Object sendSFrameLock;

	/**
	 * 接收到帧的数量最大阀值
	 */

	private short frameAmountMax;

	/**
	 * 发送消息句柄
	 */
	private ChannelHandlerContext ctx;

	public ControlManageUtil(ChannelHandlerContext ctx) {
		send = 0;
		accept = 0;
		frameAmount = 0;
		sendSFrameLock = new Object();
		sendLock = new Object();
		frameAmountMax = 1;
		this.ctx = ctx;
	}

	/**
	 * 启动S发送S确认帧 的任务
	 */
	public void startSendFrameTask() {
		Runnable runnable = () -> {
			while (ctx.channel().isActive()) {
				try {
					synchronized (sendSFrameLock) {
						if (frameAmount >= frameAmountMax) {
							// 查过最大帧 的数量就要发送一个确认帧出去
							byte[] control = Iec104Util.getSControl(accept);
							MessageDetail ruleDetail104 = new Iec104MessageDetail(control);
							ctx.channel().writeAndFlush(new Iec104Encoder().encode(ruleDetail104));
							frameAmount = 0;
						}
						sendSFrameLock.wait();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		CachedThreadPool.getCachedThreadPool().execute(runnable);
	}

	/**
	 * 返回当前的发送序号
	 */
	public short getSend() {
		synchronized (sendLock) {
			short sendRule = this.send;
			this.send++;
			if (send > Iec104Constant.SEND_MAX) {
				send = Iec104Constant.SEND_MIN;
			}
			return sendRule;
		}
	}
	public short getAccept() {
		return accept;
	} 
	
	/**
	 * @Title: setAccept
	 * @Description: 设置接收序号
	 * @param lastAccept
	 */
	public void setAccept(short lastAccept) {
		synchronized (sendSFrameLock) {
			this.accept = lastAccept;
			frameAmount++;
			if (frameAmount >= frameAmountMax) {
				this.accept = lastAccept;
				sendSFrameLock.notifyAll();
			}
		}
	}

	/**
	 * @Ttile notifySendFrameTask
	 * @Description： 结束时唤醒线程回收
	 * @return
	 */
	public void notifySendFrameTask(){
		synchronized (sendSFrameLock){
			sendSFrameLock.notifyAll();
		}
	}

	public ControlManageUtil setFrameAmountMax(short frameAmountMax) {
		this.frameAmountMax = frameAmountMax;
		return this;
	}
}
