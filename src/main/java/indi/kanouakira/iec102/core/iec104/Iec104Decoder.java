package indi.kanouakira.iec102.core.iec104;

import indi.kanouakira.iec102.core.iec104.enums.QualifiersEnum;
import indi.kanouakira.iec102.core.iec104.enums.TypeIdentifierEnum;
import indi.kanouakira.iec102.standard.Decoder;
import indi.kanouakira.iec102.standard.MessageDetail;
import indi.kanouakira.iec102.util.ByteUtil;
import indi.kanouakira.iec102.util.Iec104Util;

import java.util.ArrayList;
import java.util.List;

/**
 * IEC 60870-5-104 标准的报文解码实现。
 *
 * @author KanouAkira
 * @date 2022/4/18 22:03
 */
public class Iec104Decoder implements Decoder {

    @Override
    public MessageDetail decode(byte[] bytes) {
        Iec104MessageDetail ruleDetail104 = new Iec104MessageDetail();
        int index = 0;
        ruleDetail104.setStart(bytes[index++]);
        ruleDetail104.setApuuLength(bytes[index++] & 0xFF);
        ruleDetail104.setControl(ByteUtil.getByte(bytes, index, 4));
        index += 4;
        if (ruleDetail104.getApuuLength() <= 4) {
            ruleDetail104.setMessages(new ArrayList<>());
            // 如果只有 APCI 就此返回
            return ruleDetail104;
        }
        // 下面是返回 ASDU 的结构
        ruleDetail104.setTypeIdentifier(TypeIdentifierEnum.getTypeIdentifierEnum(bytes[index++]));
        // 添加可变结构限定词
        ruleDetail104.setChanged(bytes[index++]);
        ruleDetail104.setTransferReason(ByteUtil.byteArrayToShort(ByteUtil.getByte(bytes, index, 2)));
        index += 2;
        ruleDetail104.setTerminalAddress(Iec104Util.getTerminalAddressShort(ByteUtil.getByte(bytes, index, 2)));
        index += 2;
        ruleDetail104.setMessageAttribute();
        setMessage(ruleDetail104, bytes, index);
        return ruleDetail104;
    }


    /**
     * @param @param ruleDetail104
     * @param @param bytes
     * @param @param index
     * @return void
     * @throws
     * @Title: setMessage
     * @Description: 对消息进行解码
     */
    public static void setMessage(Iec104MessageDetail ruleDetail104, byte[] bytes, int index) {
        int messageIndex = index;
        if (ruleDetail104.isContinuous()) {
            setContinuousMessage(ruleDetail104, bytes, messageIndex);
        } else {
            setNoContinuousMessage(ruleDetail104, bytes, messageIndex);
        }
    }

    /**
     * @param ruleDetail104
     * @param bytes
     * @param index
     * @return void
     * @throws
     * @Title: setContinuousMessage
     * @Description: 设置连续地址的消息
     */
    public static void setContinuousMessage(Iec104MessageDetail ruleDetail104, byte[] bytes, int index) {
        List<Iec104MessageInfo> messages = new ArrayList<>();
        int messageIndex = index;
        // 连续的 前三个字节是地址
        // 如果是地址联系的只需要设置一个初始地址就可以了
        // TODO 此处不处理地址
        int messageAddress = Iec104Util.messageAddressToInt(ByteUtil.getByte(bytes, messageIndex, 3));
        ruleDetail104.setMessageAddress(messageAddress);
        messageIndex += 3;
        if (ruleDetail104.isMessage()) {
            // 获取每个消息的长度
            int messageLength = getMessageLength(ruleDetail104);
            int messageSize = 0;
            while (messageSize < ruleDetail104.getMessageLength()) {
                Iec104MessageInfo messageObj = new Iec104MessageInfo();
                messageObj.setMessageAddress(messageAddress);
                byte[] messageInfos = ByteUtil.getByte(bytes, messageIndex, messageLength);
                messageIndex += messageLength;
                messageObj.setMessageInfos(messageInfos);
                // 对数据的值进行解析
                setMessageValue(ruleDetail104, messageObj);
                if (ruleDetail104.isQualifiers() && TypeIdentifierEnum.isTelemetry(ruleDetail104.getTypeIdentifier())) {
                    // 判断是否有限定词
                    // 0（每个信息元素后缀1个字节）
                    ruleDetail104.setQualifiersType(QualifiersEnum.getQualifiersEnum(ruleDetail104.getTypeIdentifier(), bytes[messageIndex++]));
                }
                messageSize++;
                messageAddress++;
                messages.add(messageObj);
            }
        }
        if (ruleDetail104.isQualifiers() && !TypeIdentifierEnum.isTelemetry(ruleDetail104.getTypeIdentifier())) {
            // 判断是否有限定词
            ruleDetail104.setQualifiersType(QualifiersEnum.getQualifiersEnum(ruleDetail104.getTypeIdentifier(), bytes[messageIndex++]));
        }
        if (ruleDetail104.isTimeScaleExit()) {
            ruleDetail104.setTimeScale(ByteUtil.byte2HDate(ByteUtil.getByte(bytes, messageIndex, 7)));
        }
        ruleDetail104.setMessages(messages);
    }

    /**
     * @param ruleDetail104
     * @param bytes
     * @param index
     * @return void
     * @throws
     * @Title: setNoContinuousMessage
     * @Description: 设置不连续地址的消息
     */
    public static void setNoContinuousMessage(Iec104MessageDetail ruleDetail104, byte[] bytes, int index) {
        List<Iec104MessageInfo> messages = new ArrayList<>();
        int messageIndex = index;
        // 获取每个消息的长度
        int messageLength = getMessageLength(ruleDetail104);
        int messageSize = 0;
        while (messageSize < ruleDetail104.getMessageLength()) {
            Iec104MessageInfo messageObj = new Iec104MessageInfo();
            // 消息地址
            messageObj.setMessageAddress(Iec104Util.messageAddressToInt(ByteUtil.getByte(bytes, messageIndex, 3)));
            messageIndex += 3;
            if (ruleDetail104.isMessage()) {
                // 消息集合
                byte[] messageInfos = ByteUtil.getByte(bytes, messageIndex, messageLength);
                messageIndex += messageLength;
                messageObj.setMessageInfos(messageInfos);
                // 对数据的值进行解析
                setMessageValue(ruleDetail104, messageObj);
            } else {
                messageObj.setMessageInfos(new byte[]{});
            }
            if (ruleDetail104.isQualifiers() && TypeIdentifierEnum.isTelemetry(ruleDetail104.getTypeIdentifier())) {
                // 判断是否有限定词
                // 0（每个信息元素后缀1个字节）
                ruleDetail104.setQualifiersType(QualifiersEnum.getQualifiersEnum(ruleDetail104.getTypeIdentifier(), bytes[messageIndex++]));
            }
            messageSize++;
            messages.add(messageObj);
        }
        if (ruleDetail104.isQualifiers() && !TypeIdentifierEnum.isTelemetry(ruleDetail104.getTypeIdentifier())) {
            // 判断是否有限定词
            ruleDetail104.setQualifiersType(QualifiersEnum.getQualifiersEnum(ruleDetail104.getTypeIdentifier(), bytes[messageIndex++]));
        }
        if (ruleDetail104.isTimeScaleExit()) {
            ruleDetail104.setTimeScale(ByteUtil.byte2HDate(ByteUtil.getByte(bytes, messageIndex, 7)));
        }
        ruleDetail104.setMessages(messages);
    }

    /**
     * 根据类型对数据的值进行解析
     *
     * @param ruleDetail104
     * @param messageObj
     */
    private static void setMessageValue(Iec104MessageDetail ruleDetail104, Iec104MessageInfo messageObj) {
        switch (ruleDetail104.getTypeIdentifier().getValue()) {
            case 0x09:
                // 遥测 测量值 归一化值 遥测
                break;
            case 0x0B:
                // 遥测 测量值 标度化值 遥测
                break;
            case 0x66:
                // 读单个参数
                break;
            case (byte) 0x84:
                //  读多个参数
                break;
            case 0x30:
                // 预置单个参数命令
                break;
            case (byte) 0x88:
                // 预置多个个参数
                break;
            default:
        }
    }

    /**
     * 根据类型标识返回消息长度
     *
     * @param ruleDetail104
     * @return
     */
    private static int getMessageLength(Iec104MessageDetail ruleDetail104) {
        return ruleDetail104.getTypeIdentifier().getMessageLength();
    }
}
