package indi.kanouakira.iec102.standard;

/**
 * 信息对象编码器。
 *
 * @author KanouAkira
 * @date 2022/4/18 22:21
 */
public interface Encoder {

    /**
     * 需要将信息体对象转换为 byte 数组实现。
     * @param messageDetail
     * @return
     * @throws Exception
     */
    byte[] encode(MessageDetail messageDetail) throws Exception;

}
