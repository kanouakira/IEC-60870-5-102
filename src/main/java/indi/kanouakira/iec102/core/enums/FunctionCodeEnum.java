package indi.kanouakira.iec102.core.enums;

import java.util.Arrays;

/**
 * IEC 60870-5-102 功能码枚举。
 *
 * @author KanouAkira
 * @date 2022/4/21 12:28
 */
public enum FunctionCodeEnum {

    /* 复位通信单元 */
    RESET_COMMUNICATE_UNIT(0x00,1, 0),

    /* 传送数据 */
    TRANSFORM_DATA(0x03,1, 1),

    /* 召唤链路状态 */
    SUMMON_LINK_STATUS(0x09, 1, 0),

    /* 召唤1 级用户数据 */
    SUMMON_CLASS_ONE(0x0A,1, 1),

    /* 召唤2 级用户数据 */
    SUMMON_CLASS_TWO(0x0B,1, 1),

    /* 从站功能码 */

    /* 复位通信单元 */
    RESET_COMMUNICATE_UNIT_CONFIRM(0x00,0, null),

    /* 链路忙 */
    LINK_BUSY(0x01,0, null),

    /* 以数据响应请求, 用于变长帧 */
    DATA_RESPONSE(0x08, 0, null),

    /* 没有召唤的数据 */
    NO_DATA_AVAILABLE(0x09,0, null),

    /* 响应请求链路状态 */
    SUMMON_LINK_STATUS_CONFIRM(0x0B,0, null);


    FunctionCodeEnum(int value, Integer prm, Integer fcv) {
        this.value = (byte) value;
        this.prm = prm;
        this.fcv = fcv;
    }

    private Byte value;

    private Integer prm;

    private Integer fcv;


    public Byte getValue() {
        return value;
    }

    public Integer getPrm() {
        return prm;
    }

    public Integer getFcv() {
        return fcv;
    }

    public static FunctionCodeEnum getFunctionCodeEnum(byte functionCode, Integer prm){
        FunctionCodeEnum functionCodeEnum = Arrays.stream(FunctionCodeEnum.values())
                .filter(fc -> prm.equals(fc.prm))
                .filter(fc -> fc.value.equals(functionCode))
                .findAny().orElseThrow(() -> new RuntimeException("不存在的功能码"));
        return functionCodeEnum;
    }
}
