package indi.kanouakira.iec102.standard;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 通道处理数据实现。
 *
 * @author KanouAkira
 * @date 2022/4/18 16:47
 */
public class ChannelHandlerImpl implements ChannelHandler {

    private ChannelHandlerContext ctx;

    public ChannelHandlerImpl(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Channel getChannel() {
        return this.ctx.channel();
    }

    @Override
    public ChannelHandlerContext getChannelHandlerContext() {
        return this.ctx;
    }

    @Override
    public void writeAndFlush(MessageDetail messageDetail) {
        ctx.channel().writeAndFlush(messageDetail);
    }

}
