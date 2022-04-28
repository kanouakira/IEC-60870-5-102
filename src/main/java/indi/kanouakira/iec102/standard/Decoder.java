package indi.kanouakira.iec102.standard;

/**
 * 解码器
 *
 * @author KanouAkira
 * @date 2022/4/18 22:02
 */
public interface Decoder {

    /**
     * 需要将 byte 数组转换信息体实现。
     *
     * @param bytes 报文
     * @return
     */
    MessageDetail decode(byte[] bytes);

}
