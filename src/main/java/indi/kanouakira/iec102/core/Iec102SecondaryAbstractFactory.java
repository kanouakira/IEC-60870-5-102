package indi.kanouakira.iec102.core;

import indi.kanouakira.iec102.standard.*;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * IEC 60870-5-102 的从站抽象工厂实现。
 *
 * @author KanouAkira
 * @date 2022/4/19 17:06
 */
public class Iec102SecondaryAbstractFactory implements StandardFactory {

    private DataConfig dataConfig;

    public Iec102SecondaryAbstractFactory() {
        this(new Iec102DefaultDataConfig());
    }

    public Iec102SecondaryAbstractFactory(DataConfig dataConfig) {
        this.dataConfig = dataConfig;
    }

    @Override
    public ByteToMessageDecoder createUnpackHandler() {
        return new Iec102UnpackHandler();
    }

    @Override
    public ChannelInboundHandlerAdapter createCheckHandler() {
        return new Iec102CheckHandler();
    }

    @Override
    public Encoder createEncoder() {
        return null;
    }

    @Override
    public Decoder createDecoder() {
        return new Iec102Decoder();
    }

    @Override
    public DataHandler createDataHandler() {
        return new Iec102SecondaryDataHandler(createDataConfig());
    }

    @Override
    public DataConfig createDataConfig() {
        return dataConfig;
    }

}
