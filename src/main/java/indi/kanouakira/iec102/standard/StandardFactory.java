package indi.kanouakira.iec102.standard;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 标准协议抽象工厂接口。
 * @author KanouAkira
 * @date 2022/4/18 22:09
 */
public interface StandardFactory {

    /**
     * 协议具体粘包拆包实现。
     * @return
     */
    ByteToMessageDecoder createUnpackHandler();

    /**
     * 协议合法检查实现。
     * @return
     */
    ChannelInboundHandlerAdapter createCheckHandler();

    /**
     * 协议传输帧解码实现。
     * @return
     */
    Decoder createDecoder();

    DataHandler createDataHandler();

    DataConfig createDataConfig();

    /**
     * 拦截传输帧实现，子类可以重写。例如 IEC 60860-5-104 标准拦截U帧和S帧。
     * @param pipeline
     */
    default void interceptTransmissionFrame(ChannelPipeline pipeline){
        // 拦截U帧
        // 拦截S帧
    }

}
