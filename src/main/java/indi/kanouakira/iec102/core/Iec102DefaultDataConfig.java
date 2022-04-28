package indi.kanouakira.iec102.core;

/**
 * @author KanouAkira
 * @date 2022/4/21 11:37
 */
public class Iec102DefaultDataConfig extends Iec102DataConfig{

    public Iec102DefaultDataConfig() {
        super(AddressEnum.DOUBLE_BYTE, new byte[]{(byte) 0xFF, (byte) 0xFF});
    }

}
