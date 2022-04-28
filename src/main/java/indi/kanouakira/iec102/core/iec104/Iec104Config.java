package indi.kanouakira.iec102.core.iec104;


import indi.kanouakira.iec102.standard.DataConfig;

/**
 * 104规约的配置
 */
public class Iec104Config implements DataConfig {

    /**
     * 接收到帧的数量到该值就要发一个确认帧
     */
    private short frameAmountMax;

    /**
     * 终端地址
     */
    private short terminalAddress;

    public short getFrameAmountMax() {
        return frameAmountMax;
    }

    public void setFrameAmountMax(short frameAmountMax) {
        this.frameAmountMax = frameAmountMax;
    }

    public short getTerminalAddress() {
        return terminalAddress;
    }

    public void setTerminalAddress(short terminalAddress) {
        this.terminalAddress = terminalAddress;
    }
}
