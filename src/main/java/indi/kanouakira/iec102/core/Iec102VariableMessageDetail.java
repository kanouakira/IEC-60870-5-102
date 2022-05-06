package indi.kanouakira.iec102.core;

import indi.kanouakira.iec102.core.enums.CauseOfTransmissionEnum;
import indi.kanouakira.iec102.core.enums.FunctionCodeEnum;
import indi.kanouakira.iec102.core.enums.TypeIdentificationEnum;

import static indi.kanouakira.iec102.core.Iec102Constant.*;
import static indi.kanouakira.iec102.core.Iec102DataConfig.getConfig;
import static indi.kanouakira.iec102.util.ByteUtil.reverse;
import static indi.kanouakira.iec102.util.ByteUtil.shortToByteArray;
import static indi.kanouakira.iec102.util.Iec102Util.calcCrc8;
import static java.lang.System.arraycopy;

/**
 * 变长信息帧实体。与标准IEC 60870-5-102定义的不同，此类改造用于文件传输。
 * <pre>
 * +--------------+
 * |    起始字节    |
 * +--------------+
 * |   帧长低字节   |
 * +--------------+
 * |   帧长高字节   |
 * +--------------+
 * |    起始字节   |
 * +--------------+
 * |    控制域     |
 * +--------------+
 * |   地址低字节   |
 * +--------------+
 * |   地址高字节   |
 * +--------------+
 * |  链路用户数据  |
 * +--------------+
 * |    校验和     |
 * +--------------+
 * |   结束字节    |
 * +--------------+
 * </pre>
 *
 * @author KanouAkira
 * @date 2022/4/25 14:45
 */
public class Iec102VariableMessageDetail extends Iec102FixedMessageDetail {

    /* 帧长 */
    private short length;

    /* 应用服务数据单元 */
    private ApplicationServiceDataUnit asdu;

    @Override
    public byte[] encode() {
        int index = 0;
        byte[] bytes = new byte[6 + length];
        bytes[index++] = VARIABLE_HEAD_DATA;
        // 帧长，两字节。低位在前高位在后，所以需要翻转。
        byte[] reverse = reverse(shortToByteArray(length));
        arraycopy(reverse, 0, bytes, index, reverse.length);
        index += reverse.length;
        bytes[index++] = VARIABLE_HEAD_DATA;
        // 控制域
        bytes[index++] = super.getControl();
        // 地址，两字节。低位在前高位在后，所以需要翻转。
        byte[] address = reverse(super.getAddress());
        arraycopy(address, 0, bytes, index, address.length);
        index += address.length;
        // 链路用户数据
        byte[] asduBytes = asdu.encode();
        arraycopy(asduBytes, 0, bytes, index, asduBytes.length);
        index += asduBytes.length;
        // 校验和
        bytes[index++] = super.getCheckSum();
        bytes[index] = super.getEnd();
        return bytes;
    }

    public static Iec102VariableMessageDetail creatVariableMessageDetail(int prm, int fcbOrAcd, byte[] data, FunctionCodeEnum functionCodeEnum, TypeIdentificationEnum typeIdentificationEnum, CauseOfTransmissionEnum causeOfTransmissionEnum) {
        return creatVariableMessageDetail(prm, fcbOrAcd, functionCodeEnum.getFcv() == null ? 0 : functionCodeEnum.getFcv(), data, functionCodeEnum, typeIdentificationEnum, causeOfTransmissionEnum);
    }

    public static Iec102VariableMessageDetail creatVariableMessageDetail(int prm, int fcbOrAcd, int fcvOrDfc, byte[] data, FunctionCodeEnum functionCodeEnum, TypeIdentificationEnum typeIdentificationEnum, CauseOfTransmissionEnum causeOfTransmissionEnum) {
        byte control = calcControl(prm, fcbOrAcd, fcvOrDfc, functionCodeEnum.getValue());
        Iec102DataConfig config = getConfig();
        if (config == null)
            throw new IllegalStateException("未指定Iec102配置");
        byte[] terminalAddress = getConfig().getTerminalAddress();
        /* 计算帧长 */
        short frameLength = (short) (5 + 2 * getConfig().getAddressLength() + data.length);

        /* 封装crc校验部分字节数组 */
        byte[] crcByte = new byte[frameLength];
        int crcIndex = 0;
        crcByte[crcIndex++] = control;
        arraycopy(terminalAddress, 0, crcByte, crcIndex, terminalAddress.length);
        crcIndex += terminalAddress.length;
        crcByte[crcIndex++] = typeIdentificationEnum.getValue();
        crcByte[crcIndex++] = VARIABLE_STRUCTURE_QUALIFIER;
        crcByte[crcIndex++] = causeOfTransmissionEnum.getValue();
        arraycopy(terminalAddress, 0, crcByte, crcIndex, terminalAddress.length);
        crcIndex += terminalAddress.length;
        crcByte[crcIndex++] = RECORD_ADDRESS;
        arraycopy(data, 0, crcByte, crcIndex, data.length);

        return new Iec102VariableMessageDetail(frameLength, control, terminalAddress, typeIdentificationEnum, VARIABLE_STRUCTURE_QUALIFIER, causeOfTransmissionEnum, terminalAddress, RECORD_ADDRESS, data, calcCrc8(crcByte));
    }

    protected Iec102VariableMessageDetail(short frameLength, byte control, byte[] address,
                                          TypeIdentificationEnum typeIdentificationEnum, byte variableStructureQualifier,
                                          CauseOfTransmissionEnum causeOfTransmissionEnum, byte[] addressOfIntegratedTotal,
                                          byte recordAddress, byte[] data, byte checkSum) {
        super(control, address, checkSum);
        super.setStart(Iec102Constant.VARIABLE_HEAD_DATA);
        this.length = frameLength;
        this.asdu = new ApplicationServiceDataUnit(typeIdentificationEnum, variableStructureQualifier, causeOfTransmissionEnum, addressOfIntegratedTotal, recordAddress, data);
    }

    private class ApplicationServiceDataUnit {
        /* 类型标识 */
        private TypeIdentificationEnum typeIdentificationEnum;
        private byte typeIdentification;
        /* 可变结构限定词 1字节
        D7	            D6	D5	D4	D3	D2	D1	D0
        &0x80	        &0x7F
        SQ	            number
        地址连续性	    应用服务数据单元信息元素(单个信息元素或同类信息元素组合)的数目
        "0.地址不连续
        1.地址连续"	    <0>∶＝应用服务数据单元不含信息对象;<1..127>∶＝应用服务数据单元信息元素的数目
        */
        private byte variableStructureQualifier;
        /* 传输原因 */
        private CauseOfTransmissionEnum causeOfTransmissionEnum;
        private byte causeOfTransmission;
        /* DTE综合总地址, 可以为单字节和双字节, 本协议中使用终端地址填充  */
        private byte[] addressOfIntegratedTotal;

        /* 纪录地址 */
        private byte recordAddress;

        /* 数据区，如果为文件传输，前64字节为文件名 */
        private byte[] data;

        public ApplicationServiceDataUnit(TypeIdentificationEnum typeIdentificationEnum, byte variableStructureQualifier,
                                          CauseOfTransmissionEnum causeOfTransmissionEnum, byte[] addressOfIntegratedTotal,
                                          byte recordAddress, byte[] data) {
            this.typeIdentificationEnum = typeIdentificationEnum;
            this.typeIdentification = typeIdentificationEnum.getValue();
            this.variableStructureQualifier = variableStructureQualifier;
            this.causeOfTransmissionEnum = causeOfTransmissionEnum;
            this.causeOfTransmission = causeOfTransmissionEnum.getValue();
            this.addressOfIntegratedTotal = addressOfIntegratedTotal;
            this.recordAddress = recordAddress;
            this.data = data;
        }

        public byte[] encode() {
            byte[] bytes = new byte[4 + addressOfIntegratedTotal.length + data.length];
            int index = 0;
            bytes[index++] = typeIdentification;
            bytes[index++] = variableStructureQualifier;
            bytes[index++] = causeOfTransmission;
            arraycopy(addressOfIntegratedTotal, 0, bytes, index, addressOfIntegratedTotal.length);
            index += addressOfIntegratedTotal.length;
            bytes[index++] = recordAddress;
            arraycopy(data, 0, bytes, index, data.length);
            return bytes;
        }

        public TypeIdentificationEnum getTypeIdentificationEnum() {
            return typeIdentificationEnum;
        }

        public CauseOfTransmissionEnum getCauseOfTransmissionEnum() {
            return causeOfTransmissionEnum;
        }

        public byte[] getData() {
            return data;
        }
    }

    public TypeIdentificationEnum getType() {
        return asdu.getTypeIdentificationEnum();
    }

    public CauseOfTransmissionEnum getCause() {
        return asdu.getCauseOfTransmissionEnum();
    }

    public byte[] getData() {
        return asdu.getData();
    }

}
