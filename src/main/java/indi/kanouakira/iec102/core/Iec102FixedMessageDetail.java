package indi.kanouakira.iec102.core;

import indi.kanouakira.iec102.core.enums.FunctionCodeEnum;
import indi.kanouakira.iec102.standard.MessageDetail;
import indi.kanouakira.iec102.util.ByteUtil;

import static indi.kanouakira.iec102.core.Iec102DataConfig.getConfig;
import static indi.kanouakira.iec102.core.enums.FunctionCodeEnum.*;
import static indi.kanouakira.iec102.util.Iec102Util.calcCrc8;

/**
 * 定长信息帧实体。
 *
 * @author KanouAkira
 * @date 2022/4/20 18:20
 */
public class Iec102FixedMessageDetail extends MessageDetail implements Iec102MessageDetail {

    /* 控制域1字节 */
    private byte control;

    private FunctionCodeEnum functionCodeEnum;

    /* 地址，支持0字节、1字节、2字节形式;具体多少字节由配置类决定。低位在前 */
    private byte[] address;

    /* 校验和1字节 */
    private byte checkSum;

    /* 结束字节 */
    private final byte end = 0x16;

    /**
     * 由字节数组解码。
     *
     * @param bytes
     * @return
     */
    public static Iec102FixedMessageDetail decode(byte[] bytes) {
        int index = 0;
        // 起始字符
        byte start = bytes[index++];
        // 控制域
        byte control = bytes[index++];
        // 地址
        Iec102DataConfig config = getConfig();
        byte[] address = ByteUtil.getByte(bytes, index, config.getAddressLength());
        index += config.getAddressLength();
        // CRC校验
        byte checkSum = bytes[index++];
        // 结束字符
        byte end = bytes[index++];
        return new Iec102FixedMessageDetail(control, address, checkSum);
    }

    public byte[] encode() {
        Iec102DataConfig config = getConfig();
        if (config == null)
            throw new IllegalStateException("未指定Iec102配置");
        byte[] encode = new byte[4 + config.getAddressLength()];
        encode[0] = getStart();
        encode[1] = control;
        System.arraycopy(address, 0, encode, 2, address.length);
        encode[encode.length - 2] = checkSum;
        encode[encode.length - 1] = end;
        return encode;
    }

    /* 创建主站基本指令 */

    /**
     * 请求链路状态
     *
     * @param fcb 新的 fcb 位
     * @return
     */
    public static Iec102FixedMessageDetail summonLinkStatus(int fcb) {
        return createFixedMessageDetail(1, fcb, SUMMON_LINK_STATUS);
    }

    /**
     * 复位通信单元
     *
     * @param fcb 新的 fcb 位
     * @return
     */
    public static Iec102FixedMessageDetail resetCommunicateUnit(int fcb) {
        return createFixedMessageDetail(1, fcb, RESET_COMMUNICATE_UNIT);
    }

    /**
     * 召唤一级数据
     *
     * @param fcb 新的 fcb 位
     * @return
     */
    public static Iec102FixedMessageDetail summonClassOne(int fcb) {
        return createFixedMessageDetail(1, fcb, SUMMON_CLASS_ONE);
    }

    /**
     * 召唤二级数据
     *
     * @param fcb 新的 fcb 位
     * @return
     */
    public static Iec102FixedMessageDetail summonClassTwo(int fcb) {
        return createFixedMessageDetail(1, fcb, SUMMON_CLASS_TWO);
    }

    public static Iec102FixedMessageDetail createFixedMessageDetail(int prm, int fcbOrAcd, FunctionCodeEnum functionCodeEnum) {
        return createFixedMessageDetail(prm, fcbOrAcd, functionCodeEnum.getFcv() == null ? 0 : functionCodeEnum.getFcv(), functionCodeEnum);
    }

    public static Iec102FixedMessageDetail createFixedMessageDetail(int prm, int fcbOrAcd, int fcvOrDfc, FunctionCodeEnum functionCodeEnum) {
        byte control = calcControl(prm, fcbOrAcd, fcvOrDfc, functionCodeEnum.getValue());
        Iec102DataConfig config = getConfig();
        if (config == null)
            throw new IllegalStateException("未指定Iec102配置");
        byte[] terminalAddress = getConfig().getTerminalAddress();
        byte[] crcByte = new byte[1 + getConfig().getAddressLength()];
        crcByte[0] = control;
        System.arraycopy(terminalAddress, 0, crcByte, 1, terminalAddress.length);
        return new Iec102FixedMessageDetail(control, terminalAddress, calcCrc8(crcByte));
    }

    /* 不允许主动创建 */
    protected Iec102FixedMessageDetail(byte control, byte[] address, byte checkSum) {
        super(Iec102Constant.FIXED_HEAD_DATA);
        super.setStart(Iec102Constant.FIXED_HEAD_DATA);
        this.control = control;
        this.address = address;
        this.checkSum = checkSum;
        this.functionCodeEnum = FunctionCodeEnum.getFunctionCodeEnum(getFunctionCode(control), isFromPrimary(control));
    }

    public byte getControl() {
        return control;
    }

    public byte[] getAddress() {
        return address;
    }

    public byte getCheckSum() {
        return checkSum;
    }

    public byte getEnd() {
        return end;
    }

    protected static byte calcControl(int prm, int fcbOrAcd, int fcvOrDfc, byte functionCode) {
        if (functionCode > 0x0F)
            throw new RuntimeException("功能码不合法，请检查");

        byte control = (byte) 0b00000000;

        control ^= (((byte) prm) << 6);
        control ^= (((byte) fcbOrAcd) << 5);
        control ^= (((byte) fcvOrDfc) << 4);
        control ^= (functionCode & (byte) 0b00001111);

        return control;
    }

    private byte getFunctionCode(byte control) {
        return (byte) (control & 0b00001111);
    }

    /**
     * 1 表示消息由主站 -> 终端
     * 0 表示消息由终端 -> 主站。
     *
     * @return
     */
    private int isFromPrimary(byte control) {
        return (control & 0b01000000) >> 6;
    }

    @Override
    public int getFcbOrAcd() {
        return (control & 0b00100000) >> 5;
    }

    @Override
    public FunctionCodeEnum getFunctionCodeEnum() {
        return functionCodeEnum;
    }

}
