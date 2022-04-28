package indi.kanouakira.iec102.core.iec104;

import indi.kanouakira.iec102.standard.*;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * IEC 60870-5-104 协议主站抽象工厂
 * @author KanouAkira
 * @date 2022/4/18 22:07
 */
public class Iec104PrimaryAbstractFactory implements StandardFactory {

    @Override
    public ByteToMessageDecoder createUnpackHandler() {
        return new Iec104UnpackHandler();
    }

    @Override
    public ChannelInboundHandlerAdapter createCheckHandler() {
        return new Iec104CheckHandler();
    }

    @Override
    public Encoder createEncoder() {
        return new Iec104Encoder();
    }

    @Override
    public Decoder createDecoder() {
        return new Iec104Decoder();
    }

    @Override
    public DataHandler createDataHandler() {
        return new Iec104PrimaryDataHandler(createDataConfig());
    }

    @Override
    public DataConfig createDataConfig() {
        return new Iec104DefaultConfig();
    }

    @Override
    public void interceptTransmissionFrame(ChannelPipeline pipeline) {
        StandardFactory.super.interceptTransmissionFrame(pipeline);
        // 拦截 U帧处理器
        pipeline.addLast("uFrame", new SysUFrameClientHandler());
        // 拦截 S帧处理器
        pipeline.addLast("sFrame", new SysSFrameHandler());
    }
}
