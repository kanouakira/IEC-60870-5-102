package indi.kanouakira.iec102.server.secondary;

import indi.kanouakira.iec102.standard.StandardFactory;
import indi.kanouakira.iec102.standard.StandardInitializer;
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
public abstract class SecondaryStation {

    /* 连接端口 */
    protected int port;

    /* 连接协议的标准抽象工厂 */
    protected StandardFactory factory;

    protected SecondaryStation(int port, StandardFactory factory) {
        this.port = port;
        this.factory = factory;
    }

    /**
     * 启动从站。
     */
    public void run() throws Exception{
        // boss 负责接受连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // worker 负责网络读写
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new StandardInitializer(factory))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
