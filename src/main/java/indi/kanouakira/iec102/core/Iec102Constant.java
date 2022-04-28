package indi.kanouakira.iec102.core;

/**
 * IEC 60870-5-102 协议常量。
 *
 * @author KanouAkira
 * @date 2022/4/20 16:37
 */
public class Iec102Constant {

    /* 定长起始字符 */
    public static final byte FIXED_HEAD_DATA = 0x10;

    /* 定长校验起始坐标,同为控制域坐标 */
    public static final byte FIXED_CHECK_START_POS = 0x01;

    /* 变长起始字符 */
    public static final byte VARIABLE_HEAD_DATA = 0x68;

    /* 变长校验起始坐标,同为控制域坐标 */
    public static final byte VARIABLE_CHECK_START_POS = 0x04;

    /* 变长类型标识起始坐标 */
    public static final byte VARIABLE_TYPE_IDENTIFICATION_POS = 0x07;

    /* 变长传输原因起始坐标 */
    public static final byte VARIABLE_CAUSE_POS = 0x09;

    /* 变长数据区起始坐标 */
    public static final byte VARIABLE_DATA_START_POS = 0x0D;

    /* 变长帧字节数 */
    public static final byte FRAME_LENGTH = 0x02;

    /* 控制域长度 */
    public static final byte CONTROL_LENGTH = 0x01;

    /* 结束字符 */
    public static final byte END_DATA = 0x16;

    /* 自定义部分 */
    /* 文件长度由4个字节表示 */
    public static final byte FILE_SIZE_LENGTH = 0x04;

    /* 文件长度起始字节坐标 13字节-16字节 */
    public static final byte FILE_SIZE_START_POS = 0x0D;

    /* 可变结构限定词 */
    public static final byte VARIABLE_STRUCTURE_QUALIFIER = 0x01;

    /* 本协议改造纪录地址默认为0 */
    public static final byte RECORD_ADDRESS = 0x00;

    /* 数据区文件名占用64个字节 */
    public static final int FILENAME_BYTE_LENGTH = 64;

    /* 数据区文件内容占用字节上限512字节 */
    public static final int FILE_CONTEXT_MAX_LENGTH = 512;

}
