package indi.kanouakira.iec102.server.primary;

import indi.kanouakira.iec102.standard.StandardFactory;
import indi.kanouakira.iec102.standard.StandardInitializer;
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
public abstract class PrimaryStation {
    /* 连接主机地址 */
    protected String host;

    /* 连接端口 */
    protected int port;

    /* 连接协议的标准抽象工厂 */
    protected StandardFactory factory;

    /**
     * @param host    连接主机地址
     * @param port    连接端口
     * @param factory 连接协议的标准抽象工厂。
     */
    protected PrimaryStation(String host, int port, StandardFactory factory) {
        this.host = host;
        this.port = port;
        this.factory = factory;
    }

    /**
     * 启动主站默认实现。
     */
    void run() throws Exception {
        // boss 负责接受连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new StandardInitializer(factory));
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
        }
    }

}
