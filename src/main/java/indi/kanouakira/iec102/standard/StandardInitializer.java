package indi.kanouakira.iec102.standard;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * 从站处理链。
 *
 * @author KanouAkira
 * @date 2022/4/18 20:32
 */
public class StandardInitializer extends ChannelInitializer<SocketChannel> {

    StandardFactory standardFactory;

    public StandardInitializer(StandardFactory standardFactory) {
        this.standardFactory = standardFactory;
    }

    /**
     * 处理信息帧，通过子类抽象工厂来实现。例如以IEC 60870-5-102 实现同时沾包拆包、数据检查、信息帧处理都得用一套标准。
     * @param socketChannel
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline pipeline = socketChannel.pipeline();

        // 粘包拆包
        pipeline.addLast("unpack", standardFactory.createUnpackHandler());

        // 数据检查
        ChannelInboundHandlerAdapter checkHandler = standardFactory.createCheckHandler();
        if (checkHandler != null) {
            pipeline.addLast("check", checkHandler);
        }

        // 字节编码
        pipeline.addLast("byteEncoder", new BytesEncoder());

        // 数据编码
        pipeline.addLast("encoder", new DataEncoder());

        // 自定义拦截传输帧
        standardFactory.interceptTransmissionFrame(pipeline);

        // 数据解码
        DataDecoder dataDecoder = new DataDecoder(standardFactory.createDecoder());
        if (dataDecoder != null) {
            pipeline.addLast("decoder", dataDecoder);
        }

        // 自定义信息帧处理
        DataHandler dataHandler = standardFactory.createDataHandler();
        if (dataHandler != null) {
            pipeline.addLast("handler", new InfomationHandler(standardFactory.createDataHandler()));
        }

    }

}
