package indi.kanouakira.iec102.standard;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;

/**
 * 信息处理载入不同的 DataHandler 实现对信息进行不同的处理。
 *
 * @author KanouAkira
 * @date 2022/4/22 17:40
 */
public class InfomationHandler extends SimpleChannelInboundHandler<MessageDetail> {

    private DataHandler dataHandler;

    public InfomationHandler(DataHandler dataHandler) {
        if (dataHandler == null)
            throw new IllegalArgumentException("数据处理实现类不存在");
        this.dataHandler = dataHandler;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        dataHandler.handlerAdded(new ChannelHandlerImpl(ctx));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        dataHandler.handlerActive(new ChannelHandlerImpl(ctx));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, MessageDetail messageDetail) throws IOException {
        dataHandler.channelReceived(new ChannelHandlerImpl(ctx), messageDetail);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        System.err.println("exceptionCaught:" + incoming.remoteAddress() + "异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        dataHandler.handlerInactive(new ChannelHandlerImpl(ctx));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        dataHandler.handlerRemoved(new ChannelHandlerImpl(ctx));
    }

}

