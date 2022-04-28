package indi.kanouakira.iec102.standard;

import indi.kanouakira.iec102.core.iec104.CachedThreadPool;
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
        this.dataHandler = dataHandler;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        if (dataHandler != null) {
            Runnable runnable = () -> {
                try {
                    dataHandler.handlerAdded(new ChannelHandlerImpl(ctx));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            CachedThreadPool.getCachedThreadPool().execute(runnable);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (dataHandler != null) {
            Runnable runnable = () -> {
                try {
                    dataHandler.handlerActive(new ChannelHandlerImpl(ctx));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            CachedThreadPool.getCachedThreadPool().execute(runnable);
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, MessageDetail messageDetail) throws IOException {
        if (dataHandler != null) {
            CachedThreadPool.getCachedThreadPool().execute(() -> {
                try {
                    dataHandler.channelReceived(new ChannelHandlerImpl(ctx), messageDetail);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }
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
        if (dataHandler != null) {
            Runnable runnable = () -> {
                try {
                    dataHandler.handlerInactive(new ChannelHandlerImpl(ctx));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            CachedThreadPool.getCachedThreadPool().execute(runnable);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        if (dataHandler != null) {
            Runnable runnable = () -> {
                try {
                    dataHandler.handlerRemoved(new ChannelHandlerImpl(ctx));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            CachedThreadPool.getCachedThreadPool().execute(runnable);
        }
    }

}

