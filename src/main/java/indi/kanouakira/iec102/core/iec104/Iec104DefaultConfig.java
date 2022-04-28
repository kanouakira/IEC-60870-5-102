package indi.kanouakira.iec102.core.iec104;


/**
 * 默认的配置
 */
public class Iec104DefaultConfig extends Iec104Config {
    public Iec104DefaultConfig() {
        setFrameAmountMax((short) 1);
        setTerminalAddress((short) 1);
    }
}
