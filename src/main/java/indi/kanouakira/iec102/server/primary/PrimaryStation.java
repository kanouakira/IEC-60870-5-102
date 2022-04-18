package indi.kanouakira.iec102.server.primary;

import indi.kanouakira.iec102.core.config.DataConfig;
import indi.kanouakira.iec102.core.handler.DataHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 主站抽象类。
 *
 * @author KanouAkira
 * @date 2022/4/18 16:29
 */
public interface PrimaryStation {

    default void connect(String host, int port) throws Exception{
        // boss 负责接受连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(null);
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * 启动主站。
     */
    void run() throws Exception;

    /**
     * 设置数据处理方式。
     *
     * @return
     */
    PrimaryStation setDataHandler(DataHandler dataHandler);

    /**
     * 设置数据配置。
     *
     * @param dataConfig
     * @return
     */
    PrimaryStation setConfig(DataConfig dataConfig);

}
