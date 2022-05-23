package indi.kanouakira.iec102.util;

import indi.kanouakira.iec102.core.enums.FunctionCodeEnum;

/**
 * IEC 102 协议工具类。
 *
 * @author KanouAkira
 * @date 2022/4/19 17:46
 */
public class Iec102Util {

    /**
     * CRC8 循环累加校验
     *
     * @param data 待累加字节数组
     * @return 校验和
     */
    public static byte calcCrc8(byte[] data) {
        byte crc = 0;
        for (int j = 0; j < data.length; j++) {
            crc += data[j];
        }
        return crc;
    }

    /**
     * 获取功能码枚举。
     *
     * @param control
     * @return
     */
    public static FunctionCodeEnum getFunctionCodeEnum(byte control){
        return FunctionCodeEnum.getFunctionCodeEnum((byte) (control & 0b00001111));
   }

    public static byte getPrm(byte control){
        return (byte)((control >> 6));
    }

    public static byte getFcbOrAcd(byte control){
        return (byte)((control >> 5) & 0x01);
    }

    public static byte getFcvOrDfc(byte control){
        return (byte)((control >> 4) & 0x01);
    }

    public static void main(String[] args) {
        byte[] bytes = new byte[]{(byte) 0x7B, (byte) 0xFF,(byte) 0xFF};
        byte b = calcCrc8(bytes);
        System.out.println(Integer.toHexString(b));
    }

}
