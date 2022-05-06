package indi.kanouakira.iec102.core.enums;

import java.util.Arrays;

/**
 * IEC 60870-5-102 传输原因枚举。
 *
 * @author KanouAkira
 * @date 2022/4/20 16:37
 */
public enum CauseOfTransmissionEnum {

    /* 自发 */
    SPONTANEOUS(0x03),

    /* 初始化 */
    INITIALIZED(0x04),

    /* 请求 */
    REQUESTED(0x05),

    /* 激活 */
    ACTIVATION(0x06),

    /* 激活确认 */
    ACTIVATION_CONFIRMATION(0x07),

    /* 惰性 */
    DEACTIVATION(0x08),

    /* 惰性确认 */
    DEACTIVATION_CONFIRMATION(0x09),

    /* 激活终止，主站认为文件接收结束 */
    ACTIVATION_TERMINATION(0x0A),

    /* 所请求的数据记录不可用 */
    DATA_RECORD_NOT_AVAILABLE(0x0D),

    /* 所请求的应用服务数据单元类型不可用 */
    ASDU_TYPE_NOT_AVAILABLE(0x0E),

    /* 控制站发送的 ASDU 中的记录编号未知 */
    NOT_KNOWN_RECORD_NUMBER(0x0F),

    /* 控制站发送的 ASDU 中的地址规范未知 */
    NOT_KNOWN_ADDRESS_SPECIFICATION(0x10),

    /* 所请求的信息对象不可用 */
    INFOMATION_OBJECT_NOT_AVAILABLE(0x11),

    /* 所请求的集成期不可用 */
    INTEGRATION_PERIOD_NOT_AVAILABLE(0x12),

    /* 以下为IEC 60870-5-102 未使用的内容，自定义内容地址可能与上述有重复 */

    /* 此文件的最后一帧，文件传输结束 */
    TRANSMISSION_FINISHED(0x07),

    /* 不是此文件的最后一帧，文件还未传输结束 */
    TRANSMISSION_CONTINUE(0x08),

    /* 子站确认主站接收的文件长度和子站发送的文件长度相同，表示确认文件传送 成功，并处理此文件 */
    SEND_CONFIRM(0x0B),

    /* 子站认为主站接收的文件长度和子站发送的文件长度不相同，传送失败，并准 备重新传输该文件 */
    RESEND_CONFIRM(0x0C),

    /* 主站认为子站重复传送文件 */
    DUPLICATE_TRANSMISSION(0x0D),

    /* 子站确认文件重复，并作其他处理 */
    DUPLICATE_TRANSMISSION_CONFIRM(0x0E),

    /* 主站认为子站传送文件过长（大于 512*40 字节） */
    TRANSMISSION_OVER_LENGTH(0x0F),

    /* 子站确认认为子站传送文件过长，并作其他处理 */
    TRANSMISSION_OVER_LENGTH_CONFIRM(0x10),

    /* 主站认为子站传输文件格式不正确 */
    BAD_FILE_SPECIFICATION(0x11),

    /* 子站确认认为子站传输文件格式不正确，并作其他处理 */
    BAD_FILE_SPECIFICATION_CONFIRM(0x12),

    /* 主站认为子站传输单帧报文长度过长 */
    SINGLE_FRAME_OVER_LENGTH(0x13),

    /* 子站确认认为子站传输单帧报文长度过长，并作其他处理 */
    SINGLE_FRAME_OVER_LENGTH_CONFIRM(0x14);


    CauseOfTransmissionEnum(int value) {
        this.value = (byte) value;
    }

    private Byte value;

    public byte getValue() {
        return value;
    }

    public static CauseOfTransmissionEnum getCauseOfTransmissionEnum(byte causeOfTransmission) {
        CauseOfTransmissionEnum causeOfTransmissionEnum = Arrays.stream(CauseOfTransmissionEnum.values())
                .filter(c -> c.value.equals(causeOfTransmission))
                .findAny().orElseThrow(() -> new RuntimeException("不存在的传输原因"));
        return causeOfTransmissionEnum;
    }

}
