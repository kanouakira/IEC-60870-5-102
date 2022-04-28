package indi.kanouakira.iec102.core.iec104;

import indi.kanouakira.iec102.standard.ChannelHandler;
import indi.kanouakira.iec102.standard.DataConfig;
import indi.kanouakira.iec102.standard.DataHandler;
import indi.kanouakira.iec102.standard.MessageDetail;
import indi.kanouakira.iec102.util.ByteUtil;

/**
 * @author KanouAkira
 * @date 2022/4/18 22:43
 */
public class Iec104SecondaryDataHandler extends DataHandler {

    public Iec104SecondaryDataHandler(DataConfig dataConfig) {
        super(dataConfig);
    }

    @Override
    public void handlerAdded(ChannelHandler channelHandler) {
        System.out.println("从站:建立连接成功");
        String remoteAddress = channelHandler.getChannel().remoteAddress().toString();
        // 启动成功后根据从机配置决定开启一个收到多少I帧后回复一个S帧的任务, 结束操作 -> channelInactive(1)
        Iec104ThreadLocal.setControlPool(remoteAddress, new ControlManageUtil(channelHandler.getChannelHandlerContext()).setFrameAmountMax(((Iec104Config)dataConfig).getFrameAmountMax()));
        Iec104ThreadLocal.getControlPool(remoteAddress).startSendFrameTask();
    }

    @Override
    public void channelReceived(ChannelHandler channelHandler, MessageDetail messageDetail) {
        try {
            System.out.println(String.format("从站:收到消息帧 %s", ByteUtil.byteArrayToHexString(new Iec104Encoder().encode(messageDetail))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandler channelHandler) {
        // (1) 回收发送S帧的线程
        Iec104ThreadLocal.removeControlPool(channelHandler.getChannel().remoteAddress().toString()).notifySendFrameTask();
    }
}
