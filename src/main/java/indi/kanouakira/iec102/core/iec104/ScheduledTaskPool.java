package indi.kanouakira.iec102.core.iec104;

import indi.kanouakira.iec102.standard.MessageDetail;
import io.netty.channel.ChannelHandlerContext;

/**
 *  这是一个定时任务管理池
 * @ClassName:  ScheduledTaskPool
 * @Description:
 * @author: YDL
 * @date:   2020年5月19日 上午10:47:15
 */
public class ScheduledTaskPool {

	/**
	 * 发送消息句柄
	 */
	private ChannelHandlerContext ctx;

	/**
	 * 发送启动指令的线程
	 */
	private Thread sendStartThread;

	/**
	 * 循环发送启动指令线程的状态
	 */
	private Boolean sendStartStatus = false;

	private Object sendStartStatusLock = new Object();

	/**
	 * 发送测试指令的线程 类似心跳
	 */
	private Thread sendTestThread;

	/**
	 * 发送测试指令线程的状态
	 */
	private Boolean sendTestStatus = false;
	private Object sendTestStatusLock = new Object();

	/**
	 * 发送总召唤指令状态
	 */
	private Boolean senGeneralCallStatus = false;
	private Object senGeneralCallStatusLock = new Object();

	/**
	 * 启动指令收到确认后固定时间内发送总召唤指令
	 */
	private Thread generalCallTThread;

	public ScheduledTaskPool(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * @Title: sendStartFrame
	 * @Description: 发送启动帧
	 */
	public void sendStartFrame() {
		synchronized (sendStartStatusLock) {
			if (sendStartThread != null) {
				sendStartStatus = true;
				sendStartThread.start();
			} else {
				sendStartStatus = true;
				sendStartThread = new Thread(() -> {
					while (sendStartStatus) {
						try {
							ctx.channel().writeAndFlush(BasicInstruction104.STARTDT);
							Thread.sleep(5000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				sendStartThread.start();
			}
		}
	}

	/**
	 * @Title: stopSendStartFrame
	 * @Description: 停止发送确认帧
	 */
	public void stopSendStartFrame() {
		if (sendStartThread != null) {
			sendStartStatus = false;
		}
	}

	/**
	 * @Title: sendTestFrame
	 * @Description: 发送测试帧
	 */
	public void sendTestFrame() {
		synchronized (sendTestStatusLock) {
			if (sendTestThread != null && sendTestThread.getState() == Thread.State.TERMINATED) {
				sendTestStatus = true;
				sendTestThread.start();
			} else if (sendTestThread == null) {
				sendTestStatus = true;
				sendTestThread = new Thread(() -> {
					while (sendTestStatus) {
						try {
							ctx.channel().writeAndFlush(BasicInstruction104.TESTFR);
							Thread.sleep(5000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				sendTestThread.start();
			}
		}
	}

	/**
	 * @Title: stopSendTestFrame
	 * @Description: 停止发送测试帧及总召唤
	 */
	public void stopSendTestFrame() {
		if (sendTestThread != null || generalCallTThread != null ) {
			sendTestStatus = false;
		}
	}

	/**
	 * @Title: sendGeneralCall
	 * @Description: 发送总召唤
	 */
	public void sendGeneralCall() {
		synchronized (senGeneralCallStatusLock) {
			if (generalCallTThread != null && generalCallTThread.getState() == Thread.State.TERMINATED) {
				senGeneralCallStatus = true;
				generalCallTThread.start();
			} else if (generalCallTThread == null) {
				senGeneralCallStatus = true;
				generalCallTThread = new Thread(() -> {
					// 测试指令不发送后，也不发送总召唤
					while (sendTestStatus) {
						try {
							MessageDetail generalCallRuleDetail104 = BasicInstruction104.getGeneralCallRuleDetail104();
							ctx.channel().writeAndFlush(generalCallRuleDetail104);
							Thread.sleep(1000 * 30);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				generalCallTThread.start();
			}
		}
	}

}
