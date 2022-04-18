package indi.kanouakira.iec102.server.secondary;

import indi.kanouakira.iec102.core.config.DataConfig;
import indi.kanouakira.iec102.core.handler.DataHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 从站抽象类。
 *
 * @author KanouAkira
 * @date 2022/4/18 16:31
 */
public interface SecondaryStation {

    default void bind(int port) throws Exception{
        // boss 负责接受连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // worker 负责网络读写
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(null)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * 启动从站。
     */
    void run() throws Exception;

    /**
     * 设置数据处理方式。
     *
     * @return
     */
    SecondaryStation setDataHandler(DataHandler dataHandler);

    /**
     * 设置数据配置。
     *
     * @param dataConfig
     * @return
     */
    SecondaryStation setConfig(DataConfig dataConfig);

}
