package indi.kanouakira.iec102.core.iec104;

import indi.kanouakira.iec102.core.iec104.enums.QualifiersEnum;
import indi.kanouakira.iec102.core.iec104.enums.TypeIdentifierEnum;
import indi.kanouakira.iec102.standard.MessageDetail;
import indi.kanouakira.iec102.util.Iec104Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author KanouAkira
 * @date 2022/4/18 21:29
 */
public class Iec104MessageDetail extends MessageDetail {


    /**
     * 启动字符 固定 一个字节
     */
    private byte start = 0x68;

    /**
     * APUU 长度1个字节
     */
    private int apuuLength = 0;

    /**
     * 控制域 四个字节
     */
    private byte[] control;

    /**
     * 类型标识 1字节
     */
    private TypeIdentifierEnum typeIdentifier;

    /**
     * 可变结构限定词 1个字节
     * true SQ = 0 true 数目 number 是信息对象的数目
     * false SQ = 1 单个对象的信息元素或者信息元素的集合的数目
     */
    private boolean isContinuous;

    /**
     * 消息长度
     */
    private int messageLength;

    /**
     * 传输原因 两个字节
     */
    private short transferReason;

    /**
     * 终端地址 也就是应用服务数据单元公共地址
     */
    private short terminalAddress;

    /**
     * 消息地址 字节
     */
    private int messageAddress;

    /**
     * 消息结构
     */
    private List<Iec104MessageInfo> messages;


    /**
     * 判断是否有消息元素
     */
    private boolean isMessage;

    /**
     * 判断是否有限定词
     */
    private boolean isQualifiers;

    /**
     * 判断是否有时标
     */
    private boolean isTimeScaleExit;


    private QualifiersEnum qualifiersType;

    /**
     *
     * 时标
     */
    private Date timeScale;

    /**
     * 十六进制 字符串
     */
    private String hexString;

    public Iec104MessageDetail() {
        super((byte) 0x68);
    }



    /**
     *
     * @param control 控制域
     * @param typeIdentifierEnum 类型标识
     * @param sq 0 地址不连续 1 地址连续
     * @param isTest 传输原因 0 未试验 1 试验
     * @param isPn 肯定确认 和否定确认
     * @param transferReason 传输原因 后六个比特位
     * @param terminalAddress 服务地址
     * @param messageAddress 消息地址
     * @param messages 消息列表
     * @param timeScale 时间
     * @param qualifiers 限定词
     * @return
     */
    public Iec104MessageDetail(byte[] control, TypeIdentifierEnum typeIdentifierEnum, boolean sq,
                               boolean isTest, boolean isPn, short transferReason, short terminalAddress, int messageAddress,
                               List<Iec104MessageInfo> messages, Date timeScale, QualifiersEnum qualifiers) {
        super((byte) 0x68);
        this.control = control;
        this.typeIdentifier = typeIdentifierEnum;
        this.isContinuous = sq;
        this.messageLength = messages.size();
        this.transferReason = Iec104Util.getTransferReasonShort(isTest, isPn, transferReason);
        this.messages = messages;
        this.terminalAddress = terminalAddress;
        this.timeScale = timeScale;
        if (isContinuous) {
            // 只有连续地址才会在次设置地址，
            this.messageAddress = messageAddress;
        }
        if (timeScale != null) {
            this.isTimeScaleExit = true;
        }
        this.qualifiersType = qualifiers;
    }
    /**
     * 设置可以变限定词
     * @param byteItem
     */
    public void setChanged(byte byteItem) {
        // 第一位是 0 则是有序的
        this.isContinuous = (byteItem & 0x80) == 0 ? false : true;
        // 先将第一位数置零 然后转换成int
        this.messageLength = byteItem & (byte) 0x7F;
    }

    public void setMessageAttribute() {
        boolean isMessage =  !(TypeIdentifierEnum.generalCall.equals(this.getTypeIdentifier())  // 总召唤无此项
                || TypeIdentifierEnum.timeSynchronization.equals(this.getTypeIdentifier()) // 时钟同步
                || TypeIdentifierEnum.resetProcess.equals(this.getTypeIdentifier()) // 复位进程
                || TypeIdentifierEnum.initEnd.equals(this.getTypeIdentifier()));
        this.isMessage = isMessage;

        boolean isQualifiers = !(TypeIdentifierEnum.timeSynchronization.equals(this.getTypeIdentifier()) // 时钟同步
                || TypeIdentifierEnum.onePointTeleIndication.equals(this.getTypeIdentifier()) // 单点摇信
                || TypeIdentifierEnum.twoPointTeleIndication.equals(this.getTypeIdentifier()) // 双点摇信
                || TypeIdentifierEnum.onePointTeleControl.equals(this.getTypeIdentifier()) // 单命令遥控
                || TypeIdentifierEnum.twoPointTeleControl.equals(this.getTypeIdentifier())); // 双命令遥控
        this.isQualifiers = isQualifiers;
        boolean isTimeScale = TypeIdentifierEnum.timeSynchronization.equals(this.getTypeIdentifier()) // 时钟同步
                || TypeIdentifierEnum.onePointTimeTeleIndication.equals(this.getTypeIdentifier()) // 摇信带时标 单点
                || TypeIdentifierEnum.twoPointTimeTeleIndication.equals(this.getTypeIdentifier()); // 摇信带时标 双点
        this.isTimeScaleExit = isTimeScale;
    }

    /**
     * U 帧或者S帧
     * @param control 控制域
     */
    public Iec104MessageDetail(byte[] control) {
        super((byte) 0x68);
        this.control = control;
        this.messages = new ArrayList<>();
    }

    public byte getStart() {
        return start;
    }

    public int getApuuLength() {
        return apuuLength;
    }

    public byte[] getControl() {
        return control;
    }

    public TypeIdentifierEnum getTypeIdentifier() {
        return typeIdentifier;
    }

    public boolean isContinuous() {
        return isContinuous;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public short getTransferReason() {
        return transferReason;
    }

    public short getTerminalAddress() {
        return terminalAddress;
    }

    public int getMessageAddress() {
        return messageAddress;
    }

    public List<Iec104MessageInfo> getMessages() {
        return messages;
    }

    public boolean isMessage() {
        return isMessage;
    }

    public boolean isQualifiers() {
        return isQualifiers;
    }

    public boolean isTimeScaleExit() {
        return isTimeScaleExit;
    }

    public QualifiersEnum getQualifiersType() {
        return qualifiersType;
    }

    public Date getTimeScale() {
        return timeScale;
    }

    public String getHexString() {
        return hexString;
    }

    public void setStart(byte start) {
        this.start = start;
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    public void setApuuLength(int apuuLength) {
        this.apuuLength = apuuLength;
    }

    public void setControl(byte[] control) {
        this.control = control;
    }

    public void setTypeIdentifier(TypeIdentifierEnum typeIdentifier) {
        this.typeIdentifier = typeIdentifier;
    }

    public void setContinuous(boolean continuous) {
        isContinuous = continuous;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public void setTransferReason(short transferReason) {
        this.transferReason = transferReason;
    }

    public void setTerminalAddress(short terminalAddress) {
        this.terminalAddress = terminalAddress;
    }

    public void setMessageAddress(int messageAddress) {
        this.messageAddress = messageAddress;
    }

    public void setMessages(List<Iec104MessageInfo> messages) {
        this.messages = messages;
    }

    public void setMessage(boolean message) {
        isMessage = message;
    }

    public void setQualifiers(boolean qualifiers) {
        isQualifiers = qualifiers;
    }

    public void setTimeScaleExit(boolean timeScaleExit) {
        isTimeScaleExit = timeScaleExit;
    }

    public void setQualifiersType(QualifiersEnum qualifiersType) {
        this.qualifiersType = qualifiersType;
    }

    public void setTimeScale(Date timeScale) {
        this.timeScale = timeScale;
    }

    public void setHexString(String hexString) {
        this.hexString = hexString;
    }
}
