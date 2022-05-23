package indi.kanouakira.iec102.core.enums;

import java.util.Arrays;

/**
 * IEC 60870-5-102 类型标识枚举。
 * 枚举仅用于主站判断使用。
 *
 * @author KanouAkira
 * @date 2022/4/20 16:36
 */
public enum TypeIdentificationEnum {

    /* IEC 60870-5-102 类型标识 这里不使用所以不列举。
    监控器方向上的处理信息
        <0> := Not defined
        <1> := Single-point information with time tag M_SP_TA_2
        <2> := Accounting integrated totals, four octets each M_IT_TA_2
        <3> := Accounting integrated totals, three octets each M_IT_TB_2
        <4> := Accounting integrated totals, two octets each M_IT_TC_2
        <5> := Periodically reset accounting integrated totals, four octets each M_IT_TD_2
        <6> := Periodically reset accounting integrated totals, three octets each M_IT_TE_2
        <7> := Periodically reset accounting integrated totals, two octets each M_IT_TF_2
        <8> := Operational integrated totals, four octets each M_IT_TG_2
        <9> := Operational integrated totals, three octets each M_IT_TH_2
        <10> := Operational integrated totals, two octets each M_IT_TI_2
        <11> := Periodically reset operational integrated totals, four octets each M_IT_TK_2
        <12> := Periodically reset operational integrated totals, three octets each M_IT_TL_2
        <13> := Periodically reset operational integrated totals, two octets each M_IT_TM_2
        <14..69> := reserved for further compatible definitions
    监控器方向上的系统信息
        <70> := End of initialization M_EI_NA_2
        <71> := Manufacturer and product specification of integrated total DTE P_MP_NA_2
        <72> := Current system time of integrated total DTE M_TI_TA_2
        <73..99> := reserved for further compatible definitions
    在控制方向上的系统信息
        CON<100> := Read manufacturer and product specification C_RD_NA_2
        CON<101> := Read record of single-point information with time tag C_SP_NA_2
        CON<102> := Read record of single-point information with time tag of a selected time range C_SP_NB_2
        CON<103> := Read current system time of integrated total DTE C_TI_NA_2
        CON<104> := Read accounting integrated totals of the oldest integration period C_CI_NA_2
        CON<105> := Read accounting integrated totals of the oldest integration period and of a selected range of addresses C_CI_NB_2
        CON<106> := Read accounting integrated totals of a specific past integration period C_CI_NC_2
        CON<107> := Read accounting integrated totals of a specific past integration period and of a selected range of addresses C_CI_ND_2
        CON<108> := Read periodically reset accounting integrated totals of the oldest integration period C_CI_NE_2
        CON<109> := Read periodically reset accounting integrated totals of the oldest integration period and of a selected range of addresses C_CI_NF_2
        CON<110> := Read periodically reset accounting integrated totals of a specific past integration period C_CI_NG_2
        CON<111> := Read periodically reset accounting integrated totals of a specific past integration period and of a selected range of addresses C_CI_NH_2
        CON<112> := Read operational integrated totals of the oldest integration period C_CI_NI_2
        CON<113> := Read operational integrated totals of the oldest integration period and of a selected range of addresses C_CI_NK_2
        CON<114> := Read operational integrated totals of a specific past integration period C_CI_NL_2
        CON<115> := Read operational integrated totals of a specific past integration period and of a selected range of addresses C_CI_NM_2
        CON<116> := Read periodically reset operational integrated totals of the oldest integration period C_CI_NN_2
        CON<117> := Read periodically reset operational integrated totals of the oldest integration period and of a selected range of addresses C_CI_NO_2
        CON<118> := Read periodically reset operational integrated totals of a specific past integration period C_CI_NP_2
        CON<119> := Read periodically reset operational integrated totals of a specific past integration period and of a selected range of addresses C_CI_NQ_2
        CON<120> := Read accounting integrated totals of a selected time range and of a selected range of addresses C_CI_NR_2
        CON<121> := Read periodically reset accounting integrated totals of a selected time range and of a selected range of addresses C_CI_NS_2
        CON<122> := Read operational integrated totals of a selected time range and of a selected range of addresses C_CI_NT_2
        CON<123> := Read periodically reset operational integrated totals of a selected time range and of a selected range of addresses C_CI_NU_2
        <124..127> := reserved for further compatible definitions
     */

    /* 以下为自定义拓展内容 */

    /* 短期预测文件 */
    DQYC(0x90),

    /* 超短期预测文件 */
    CDQYC(0x91),

    /* 风电场总体信息/气象环境监视信息报文 */
    ZTXX_QXHJ(0x92),

    /* 风力发电机组信息、或光伏逆变器/汇流箱 信息报文 */
    NBQ(0x93),

    /* 未来72小时nwp数据（备用） */
    NWP(0x94),

    /* 日检修数据（备用） */
    DAILY_MAINTENANCE(0x95),

    /* 传输结束 */
    TRANSFORM_END(0x96),

    /* 重复传输 */
    TRANSFORM_REPEAT(0x97),

    /* 文件超长 */
    FILE_OVER_LENGTH(0x98),

    /* 不符合格式 */
    BAD_SPECIFICATION(0x99),

    /* 单帧报文超长 */
    FRAME_OVER_LENGTH(0x9A),

    /* 主站对时 */
    TIME_CALIBRATION(0x67),

    /* 新规范逆变器信息报文(或风机信息报文) */
    NEW_NBQ(0xAC),

    /* 新规范总体信息报文 */
    XB_FZ(0xAD),

    /* 新规范AGC-AVC信息报文（备用） */
    NEW_AGC_AVC(0xAE),

    /* 新规范短期功率预测信息报文 */
    NEW_DQYC(0xAF),

    /* 新规范超短期功率预测信息报文 */
    NEW_CDQYC(0xB0),

    /* 升压站信息报文（备用） */
    NBQ_HLX(0xB1);


    TypeIdentificationEnum(int value) {
        this.value = (byte) value;
    }

    private Byte value;

    public byte getValue() {
        return value;
    }

    public static TypeIdentificationEnum getTypeIdentificationEnum(byte typeIdentification) {
        TypeIdentificationEnum typeIdentificationEnum = Arrays.stream(TypeIdentificationEnum.values())
                .filter(t -> t.value.equals(typeIdentification))
                .findAny().orElseThrow(() -> new RuntimeException("不存在的类型标识"));
        return typeIdentificationEnum;
    }

}

