package indi.kanouakira.iec102.core.handler;

import indi.kanouakira.iec102.core.structure.MessageDetail;
import io.netty.channel.ChannelHandler;

/**
 * 数据处理方式抽象。
 *
 * @author KanouAkira
 * @date 2022/4/18 16:46
 */
public interface DataHandler {

    /**
     * @param channelHandler 处理器
     * @Title: handlerAdded
     * @Description: 建立连接时处理操作
     */
    void handlerAdded(ChannelHandler channelHandler);

    /**
     * @param channelHandler 处理器
     * @param messageDetail  传输帧即信息
     * @Title: channelReceived
     * @Description: 收到信息时处理操作
     */
    void channelReceived(ChannelHandler channelHandler, MessageDetail messageDetail);

}
