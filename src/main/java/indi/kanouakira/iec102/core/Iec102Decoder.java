package indi.kanouakira.iec102.core;

import indi.kanouakira.iec102.core.enums.CauseOfTransmissionEnum;
import indi.kanouakira.iec102.core.enums.FunctionCodeEnum;
import indi.kanouakira.iec102.standard.Decoder;
import indi.kanouakira.iec102.standard.MessageDetail;
import indi.kanouakira.iec102.util.ByteUtil;

import static indi.kanouakira.iec102.core.Iec102Constant.*;
import static indi.kanouakira.iec102.core.Iec102FixedMessageDetail.createFixedMessageDetail;
import static indi.kanouakira.iec102.core.Iec102VariableMessageDetail.creatVariableMessageDetail;
import static indi.kanouakira.iec102.core.enums.CauseOfTransmissionEnum.getCauseOfTransmissionEnum;
import static indi.kanouakira.iec102.util.Iec102Util.*;
import static java.lang.System.arraycopy;

/**
 * 解码器，在此处将字节数组报文解码成 IEC 102 信息对象。
 *
 * @author KanouAkira
 * @date 2022/4/22 16:44
 */
public class Iec102Decoder implements Decoder {

    @Override
    public MessageDetail decode(byte[] bytes) {
        // 解析byte 数组生成对象
        MessageDetail messageDetail;
        if (bytes[0] == FIXED_HEAD_DATA) {
            byte control = bytes[FIXED_CHECK_START_POS];
            FunctionCodeEnum functionCodeEnum = getFunctionCodeEnum(control);
            messageDetail = createFixedMessageDetail(getPrm(control), getFcbOrAcd(control), getFcvOrDfc(control), functionCodeEnum);
        } else if (bytes[0] == VARIABLE_HEAD_DATA) {
            byte control = bytes[VARIABLE_CHECK_START_POS];
            // 获取功能码、类型标识、传输原因
            FunctionCodeEnum functionCodeEnum = getFunctionCodeEnum(control);
            byte typeIdentification = bytes[VARIABLE_TYPE_IDENTIFICATION_POS];
            CauseOfTransmissionEnum causeOfTransmissionEnum = getCauseOfTransmissionEnum(bytes[VARIABLE_CAUSE_POS]);
            // 解码用户数据内容
            int dataLength = bytes.length - 2 - VARIABLE_DATA_START_POS;
            byte[] data = new byte[dataLength];
            arraycopy(bytes, VARIABLE_DATA_START_POS, data, 0, dataLength);
            // 构造变长帧
            messageDetail = creatVariableMessageDetail(getPrm(control), getFcbOrAcd(control), getFcvOrDfc(control), data, functionCodeEnum, typeIdentification, causeOfTransmissionEnum);
        } else {
            throw new RuntimeException(String.format("错误的报文:%s", ByteUtil.byteArrayToHexString(bytes)));
        }
        return messageDetail;
    }

}
