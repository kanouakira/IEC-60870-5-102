package indi.kanouakira.iec102.standard;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 通道处理数据接口。
 *
 * @author KanouAkira
 * @date 2022/4/18 16:47
 */
public interface ChannelHandler {

    void writeAndFlush(MessageDetail messageDetail);

    Channel getChannel();

    ChannelHandlerContext getChannelHandlerContext();

}

