package indi.kanouakira.iec102.core;

import indi.kanouakira.iec102.core.enums.FunctionCodeEnum;
import indi.kanouakira.iec102.standard.Decoder;
import indi.kanouakira.iec102.standard.MessageDetail;
import indi.kanouakira.iec102.util.ByteUtil;

import static indi.kanouakira.iec102.core.Iec102Constant.*;
import static indi.kanouakira.iec102.core.Iec102FixedMessageDetail.createFixedMessageDetail;
import static indi.kanouakira.iec102.util.Iec102Util.*;

/**
 * @author KanouAkira
 * @date 2022/4/22 16:44
 */
public class Iec102Decoder implements Decoder {

    @Override
    public MessageDetail decode(byte[] bytes) {
        // 解析byte 数组生成对象
        MessageDetail messageDetail = null;
        if (bytes[0] == FIXED_HEAD_DATA){
            byte control = bytes[FIXED_CHECK_START_POS];
            FunctionCodeEnum functionCodeEnum = getFunctionCodeEnum(control);
            messageDetail = createFixedMessageDetail(getPrm(control), getFcbOrAcd(control), getFcvOrDfc(control), functionCodeEnum);
        }else if (bytes[0] == VARIABLE_HEAD_DATA){
            byte control = bytes[VARIABLE_CHECK_START_POS];
            FunctionCodeEnum functionCodeEnum = getFunctionCodeEnum(control);
//            messageDetail =
        }else {
            throw new RuntimeException(String.format("错误的报文:%s", ByteUtil.byteArrayToHexString(bytes)));
        }
        return messageDetail;
    }

}
