package indi.kanouakira.iec102.server.primary;

import indi.kanouakira.iec102.core.Iec102CheckHandler;
import indi.kanouakira.iec102.core.Iec102Decoder;
import indi.kanouakira.iec102.core.Iec102DefaultDataConfig;
import indi.kanouakira.iec102.core.Iec102UnpackHandler;
import indi.kanouakira.iec102.standard.DataConfig;
import indi.kanouakira.iec102.standard.StandardFactory;
import indi.kanouakira.iec102.standard.Decoder;
import indi.kanouakira.iec102.standard.DataHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author KanouAkira
 * @date 2022/5/23 11:20
 */
public class Iec102PrimaryAbstractFactory implements StandardFactory {

    private DataConfig dataConfig;

    public Iec102PrimaryAbstractFactory() {
        this(new Iec102DefaultDataConfig());
    }

    public Iec102PrimaryAbstractFactory(DataConfig dataConfig) {
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
    public Decoder createDecoder() {
        return new Iec102Decoder();
    }

    @Override
    public DataHandler createDataHandler() {
        return new Iec102PrimaryDataHandler(dataConfig);
    }

    @Override
    public DataConfig createDataConfig() {
        return dataConfig;
    }

}
