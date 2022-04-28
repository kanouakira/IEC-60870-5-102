package indi.kanouakira.iec102.core.iec104;

import indi.kanouakira.iec102.core.iec104.enums.TypeIdentifierEnum;
import indi.kanouakira.iec102.standard.Encoder;
import indi.kanouakira.iec102.standard.MessageDetail;
import indi.kanouakira.iec102.util.ByteUtil;
import indi.kanouakira.iec102.util.Iec104Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author KanouAkira
 * @date 2022/4/18 22:27
 */
public class Iec104Encoder implements Encoder {

    @Override
    public byte[] encode(MessageDetail messageDetail) throws Exception {
        if (!(messageDetail instanceof Iec104MessageDetail)){
            return null;
        }
        Iec104MessageDetail ruleDetail104 = (Iec104MessageDetail) messageDetail;
        ruleDetail104.setMessageAttribute();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bytes.write(ruleDetail104.getStart());
        byte[] apduBytes = getApduBytes(ruleDetail104);
        int messageLen =  apduBytes.length;
        ruleDetail104.setApuuLength(messageLen);
        bytes.write((byte) messageLen);
        bytes.write(apduBytes);
        return bytes.toByteArray();
    }

    private static byte[] getApduBytes(Iec104MessageDetail ruleDetail104) throws IOException {
        ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
        // 控制域
        bOutput.write(ruleDetail104.getControl());
        if (ruleDetail104.getTypeIdentifier() == null) {
            // U帧或者S帧
            return bOutput.toByteArray();
        }
        // 类型标识
        bOutput.write(ruleDetail104.getTypeIdentifier().getValue());
        // 可变结构限定词
        bOutput.write(Iec104Util.getChangedQualifiers(ruleDetail104));
        // 传输原因
        bOutput.write(ByteUtil.shortToByteArray(ruleDetail104.getTransferReason()));
        // 终端地址
        bOutput.write((Iec104Util.getTerminalAddressByte(ruleDetail104.getTerminalAddress())));
        // 如果是是连续的则数据地址 只需要在开头写以后的数据单元就不需要再写了
        if (ruleDetail104.isContinuous()) {
            bOutput.write(Iec104Util.intToMessageAddress(ruleDetail104.getMessageAddress()));
            // 地址只取三个字节
            if (ruleDetail104.isMessage()) {
                for (Iec104MessageInfo ruleDetail104Message : ruleDetail104.getMessages()) {
                    bOutput.write(ruleDetail104Message.getMessageInfos());
                    if (ruleDetail104.isQualifiers() &&  TypeIdentifierEnum.isTelemetry(ruleDetail104.getTypeIdentifier())) {
                        // 0（每个信息元素后缀1个字节）
                        bOutput.write(ruleDetail104.getQualifiersType().getValue());
                    }

                }
            }
            if (ruleDetail104.isQualifiers() && !TypeIdentifierEnum.isTelemetry(ruleDetail104.getTypeIdentifier())) {
                bOutput.write(ruleDetail104.getQualifiersType().getValue());
            }
            if (ruleDetail104.isTimeScaleExit()) {
                bOutput.write(ByteUtil.date2HByte(ruleDetail104.getTimeScale()));
            }
        } else {
            for (Iec104MessageInfo ruleDetail104Message : ruleDetail104.getMessages()) {
                bOutput.write(Iec104Util.intToMessageAddress(ruleDetail104Message.getMessageAddress()));
                if (ruleDetail104.isMessage()) {
                    bOutput.write(ruleDetail104Message.getMessageInfos());
                }
                if (ruleDetail104.isQualifiers() && TypeIdentifierEnum.isTelemetry(ruleDetail104.getTypeIdentifier())) {
                    // 0（每个信息元素后缀1个字节）
                    bOutput.write(ruleDetail104.getQualifiersType().getValue());
                }
            }
            if (ruleDetail104.isQualifiers() && !TypeIdentifierEnum.isTelemetry(ruleDetail104.getTypeIdentifier())) {
                bOutput.write(ruleDetail104.getQualifiersType().getValue());
            }
            if (ruleDetail104.isTimeScaleExit()) {
                bOutput.write(ByteUtil.date2HByte(ruleDetail104.getTimeScale()));
            }
        }
        return bOutput.toByteArray();
    }
}
