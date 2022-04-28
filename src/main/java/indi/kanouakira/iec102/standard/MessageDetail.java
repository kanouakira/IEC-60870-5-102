package indi.kanouakira.iec102.standard;

/**
 * 协议结构体，对应传输帧 Transmission Frame Formats。
 * @author KanouAkira
 * @date 2022/4/18 17:05
 */
public abstract class MessageDetail {
    /* 启动字符 */
    private byte start;

    public MessageDetail(byte start) {
        this.start = start;
    }

    protected byte getStart() {
        return start;
    }

    protected void setStart(byte start) {
        this.start = start;
    }

    /**
     * 编码操作，将对象编码成字节数组。
     * @return
     */
    abstract public byte[] encode();
}