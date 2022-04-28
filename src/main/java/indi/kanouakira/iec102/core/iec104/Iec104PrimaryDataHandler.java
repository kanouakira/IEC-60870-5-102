package indi.kanouakira.iec102.core.iec104;

import indi.kanouakira.iec102.standard.*;
import indi.kanouakira.iec102.util.ByteUtil;

/**
 * @author KanouAkira
 * @date 2022/4/18 22:43
 */
public class Iec104PrimaryDataHandler extends DataHandler {

    public Iec104PrimaryDataHandler(DataConfig dataConfig) {
        super(dataConfig);
    }

    @Override
    public void handlerAdded(ChannelHandler channelHandler) {
        System.out.println("主站:建立连接成功");
        String remoteAddress = channelHandler.getChannel().remoteAddress().toString();
        // 启动成功后发送启动链路命令，待收到从机回复的启动指令确认指令后开启一个定时发送测试指令的任务, 结束操作 -> channelInactive(1)
        Iec104ThreadLocal.setScheduledTaskPool(new ScheduledTaskPool(channelHandler.getChannelHandlerContext()));
        Iec104ThreadLocal.getScheduledTaskPool().sendStartFrame();
        // 启动成功后根据主机配置决定开启一个收到多少I帧后回复一个S帧的任务, 结束操作 -> channelInactive(2)
        Iec104ThreadLocal.setControlPool(remoteAddress, new ControlManageUtil(channelHandler.getChannelHandlerContext()).setFrameAmountMax(((Iec104Config)dataConfig).getFrameAmountMax()));
        Iec104ThreadLocal.getControlPool(remoteAddress).startSendFrameTask();
    }

    @Override
    public void channelReceived(ChannelHandler channelHandler, MessageDetail messageDetail) {
        try {
            System.out.println(String.format("主站:收到消息帧 %s", ByteUtil.byteArrayToHexString(new Iec104Encoder().encode(messageDetail))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandler channelHandler) {
        String remoteAddress = channelHandler.getChannel().remoteAddress().toString();
        // (1) 回收发送测试指令的线程
        Iec104ThreadLocal.getScheduledTaskPool().stopSendTestFrame();
        // (2) 回收发送S帧的线程
        Iec104ThreadLocal.removeControlPool(remoteAddress).notifySendFrameTask();
    }
}
